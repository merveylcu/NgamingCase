package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ConfirmDeletePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.IsPostCacheEmptyUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RefreshPostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RestorePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.SoftDeletePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val observePosts: ObservePostsUseCase,
    private val isPostCacheEmpty: IsPostCacheEmptyUseCase,
    private val refreshPosts: RefreshPostsUseCase,
    private val softDeletePost: SoftDeletePostUseCase,
    private val restorePost: RestorePostUseCase,
    private val confirmDeletePost: ConfirmDeletePostUseCase,
) : ViewModel() {

    private val requestState = MutableStateFlow(PostListUiState(isLoading = true))

    val uiState: StateFlow<PostListUiState> =
        combine(observePosts(), requestState) { posts, state ->
            state.copy(posts = posts.toImmutableList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = requestState.value,
        )

    private val _uiEffect =
        MutableSharedFlow<PostListUiEffect>(extraBufferCapacity = EFFECT_BUFFER_CAPACITY)
    val uiEffect: SharedFlow<PostListUiEffect> = _uiEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            if (isPostCacheEmpty()) {
                load()
            } else {
                requestState.update { it.copy(isLoading = false) }
                refreshPosts()
            }
        }
    }

    fun onRefresh() {
        if (requestState.value.isRefreshing) return
        viewModelScope.launch {
            requestState.update { it.copy(isRefreshing = true) }
            val result = refreshPosts()
            requestState.update { it.copy(isRefreshing = false) }
            if (result is RestResult.Error) {
                if (isPostCacheEmpty()) {
                    requestState.update { it.copy(error = result.error) }
                } else {
                    _uiEffect.emit(PostListUiEffect.ShowError(result.error))
                }
            } else {
                requestState.update { it.copy(error = null) }
            }
        }
    }

    fun onDelete(postId: Int) {
        viewModelScope.launch {
            softDeletePost(postId)
            _uiEffect.emit(PostListUiEffect.ShowUndoDelete(postId))
        }
    }

    fun onUndoDelete(postId: Int) {
        viewModelScope.launch { restorePost(postId) }
    }

    fun onDeleteConfirm(postId: Int) {
        viewModelScope.launch {
            val result = confirmDeletePost(postId)
            if (result is RestResult.Error) _uiEffect.emit(PostListUiEffect.ShowError(result.error))
        }
    }

    fun onRetry() {
        viewModelScope.launch { load() }
    }

    private suspend fun load() {
        requestState.update { it.copy(isLoading = true, error = null) }
        val result = refreshPosts()
        requestState.update {
            it.copy(
                isLoading = false,
                error = (result as? RestResult.Error)?.error,
            )
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val EFFECT_BUFFER_CAPACITY = 16
    }
}
