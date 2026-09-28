package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.UiText
import com.merveylcu.ngamingcase.core.common.base.DialogState
import com.merveylcu.ngamingcase.core.designsystem.base.BaseViewModel
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.UpdatePostUseCase
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel(assistedFactory = PostDetailViewModel.Factory::class)
class PostDetailViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val savedStateHandle: SavedStateHandle,
    private val observePost: ObservePostUseCase,
    private val updatePost: UpdatePostUseCase,
) : BaseViewModel<PostDetailUiState, PostDetailUiEffect>() {

    init {
        combine(
            observePost(postId),
            savedStateHandle.getStateFlow<String?>(KEY_TITLE, null),
            savedStateHandle.getStateFlow<String?>(KEY_BODY, null),
        ) { post, title, body ->
            PostDetailUiState(
                post = post,
                title = title ?: post?.title.orEmpty(),
                body = body ?: post?.body.orEmpty(),
            )
        }.onEach(::setContent).launchIn(viewModelScope)
    }

    fun onTitleChange(title: String) {
        savedStateHandle[KEY_TITLE] = title
    }

    fun onBodyChange(body: String) {
        savedStateHandle[KEY_BODY] = body
    }

    fun onSave() {
        val state = currentContent ?: return
        if (!state.canSave) return
        updatePost(id = postId, title = state.title, body = state.body).request {
            emitEffect(PostDetailUiEffect.NavigateBack)
        }
    }

    fun onBack() {
        val state = currentContent
        when {
            state == null -> emitEffect(PostDetailUiEffect.NavigateBack)
            state.isDirty -> showDiscardDialog()
            else -> emitEffect(PostDetailUiEffect.NavigateBack)
        }
    }

    private fun showDiscardDialog() {
        showDialog(
            DialogState(
                message = UiText.StringResource(R.string.post_detail_discard_message),
                confirmText = UiText.StringResource(R.string.post_detail_discard_confirm),
                onConfirm = ::onDiscardConfirm,
                onDismiss = ::dismissDialog,
                title = UiText.StringResource(R.string.post_detail_discard_title),
                dismissText = UiText.StringResource(R.string.post_detail_discard_dismiss),
            ),
        )
    }

    private fun onDiscardConfirm() {
        dismissDialog()
        emitEffect(PostDetailUiEffect.NavigateBack)
    }

    @AssistedFactory
    interface Factory {
        fun create(postId: Int): PostDetailViewModel
    }

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
    }
}
