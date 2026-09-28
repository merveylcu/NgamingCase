package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.designsystem.base.BaseViewModel
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ConfirmDeletePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.IsPostCacheEmptyUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RefreshPostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RestorePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.SoftDeletePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private var latestPosts: ImmutableList<Post> = persistentListOf()
    private var isInitialLoadPending = true

    init {
        observePosts().onEach(::onPostsChanged).launchIn(viewModelScope)
        viewModelScope.launch {
            if (isPostCacheEmpty()) {
                load()
            } else {
                isInitialLoadPending = false
                showPosts()
                refreshPosts().request(showLoading = false, onError = {})
            }
        }
    }

    fun onRefresh() {
        if (currentContent?.isRefreshing == true) return
        updateContent { copy(isRefreshing = true) }
        refreshPosts().request(
            showLoading = false,
            onError = { error ->
                updateContent { copy(isRefreshing = false) }
                handleError(error)
            },
            onSuccess = { updateContent { copy(isRefreshing = false) } },
        )
    }

    fun onRetry() {
        setState(BaseUiState.Loading)
        isInitialLoadPending = true
        load()
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
        confirmDeletePost(postId).request(showLoading = false)
    }

    private fun load() {
        refreshPosts().request(
            showLoading = false,
            onError = { error ->
                isInitialLoadPending = false
                handleError(error)
            },
            onSuccess = {
                isInitialLoadPending = false
                showPosts()
            },
        )
    }

    private fun onPostsChanged(posts: List<Post>) {
        latestPosts = posts.toImmutableList()
        if (isInitialLoadPending && latestPosts.isEmpty()) return
        showPosts()
    }

    private fun showPosts() {
        setContent(
            currentContent?.copy(posts = latestPosts) ?: PostListUiState(posts = latestPosts),
        )
    }
}
