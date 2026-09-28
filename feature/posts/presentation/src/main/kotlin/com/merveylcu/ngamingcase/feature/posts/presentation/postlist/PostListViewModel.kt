package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.designsystem.base.BaseViewModel
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ConfirmDeletePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.IsPostCacheEmptyUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RefreshPostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RestorePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.SoftDeletePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
) : BaseViewModel<PostListUiState, PostListUiEffect>() {

    private val requestState = MutableStateFlow(RequestState(isLoading = true))

    init {
        combine(observePosts(), requestState) { posts, request ->
            when {
                posts.isEmpty() && request.isLoading -> BaseUiState.Loading

                posts.isEmpty() && request.error != null -> BaseUiState.Error(request.error)

                else -> BaseUiState.Content(
                    PostListUiState(
                        posts = posts.toImmutableList(),
                        isRefreshing = request.isRefreshing,
                    ),
                )
            }
        }.onEach(::setState).launchIn(viewModelScope)

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
                    emitEffect(PostListUiEffect.ShowError(result.error))
                }
            } else {
                requestState.update { it.copy(error = null) }
            }
        }
    }

    fun onDelete(postId: Int) {
        viewModelScope.launch {
            softDeletePost(postId)
            emitEffect(PostListUiEffect.ShowUndoDelete(postId))
        }
    }

    fun onUndoDelete(postId: Int) {
        viewModelScope.launch { restorePost(postId) }
    }

    fun onDeleteConfirm(postId: Int) {
        viewModelScope.launch {
            val result = confirmDeletePost(postId)
            if (result is RestResult.Error) emitEffect(PostListUiEffect.ShowError(result.error))
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

    private data class RequestState(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: ErrorEntity? = null,
    )
}
