package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.UpdatePostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDetailViewModel.Factory::class)
class PostDetailViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val savedStateHandle: SavedStateHandle,
    private val observePost: ObservePostUseCase,
    private val updatePost: UpdatePostUseCase,
) : ViewModel() {

    private val isSaving = MutableStateFlow(false)
    private val isDiscardDialogVisible = MutableStateFlow(false)

    val uiState: StateFlow<PostDetailUiState> = combine(
        observePost(postId),
        savedStateHandle.getStateFlow<String?>(KEY_TITLE, null),
        savedStateHandle.getStateFlow<String?>(KEY_BODY, null),
        isSaving,
        isDiscardDialogVisible,
    ) { post, title, body, saving, discardDialogVisible ->
        PostDetailUiState(
            post = post,
            title = title ?: post?.title.orEmpty(),
            body = body ?: post?.body.orEmpty(),
            isLoading = false,
            isSaving = saving,
            notFound = post == null,
            isDiscardDialogVisible = discardDialogVisible,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = PostDetailUiState(),
    )

    private val _uiEffect =
        MutableSharedFlow<PostDetailUiEffect>(extraBufferCapacity = EFFECT_BUFFER_CAPACITY)
    val uiEffect: SharedFlow<PostDetailUiEffect> = _uiEffect.asSharedFlow()

    fun onTitleChange(title: String) {
        savedStateHandle[KEY_TITLE] = title
    }

    fun onBodyChange(body: String) {
        savedStateHandle[KEY_BODY] = body
    }

    fun onSave() {
        val state = uiState.value
        if (!state.canSave) return
        isSaving.value = true
        viewModelScope.launch {
            when (val result = updatePost(id = postId, title = state.title, body = state.body)) {
                is RestResult.Success -> _uiEffect.emit(PostDetailUiEffect.NavigateBack)

                is RestResult.Error -> {
                    isSaving.value = false
                    _uiEffect.emit(PostDetailUiEffect.ShowError(result.error))
                }
            }
        }
    }

    fun onBack() {
        val state = uiState.value
        when {
            state.isSaving -> Unit
            state.isDirty -> isDiscardDialogVisible.value = true
            else -> viewModelScope.launch { _uiEffect.emit(PostDetailUiEffect.NavigateBack) }
        }
    }

    fun onDiscardConfirm() {
        isDiscardDialogVisible.value = false
        viewModelScope.launch { _uiEffect.emit(PostDetailUiEffect.NavigateBack) }
    }

    fun onDiscardDismiss() {
        isDiscardDialogVisible.value = false
    }

    @AssistedFactory
    interface Factory {
        fun create(postId: Int): PostDetailViewModel
    }

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val EFFECT_BUFFER_CAPACITY = 16
    }
}
