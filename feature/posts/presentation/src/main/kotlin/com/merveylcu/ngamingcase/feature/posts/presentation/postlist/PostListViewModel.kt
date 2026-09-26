package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.IsPostCacheEmptyUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RefreshPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    observePosts: ObservePostsUseCase,
    private val isPostCacheEmpty: IsPostCacheEmptyUseCase,
    private val refreshPosts: RefreshPostsUseCase,
) : ViewModel() {

    // Everything except the posts themselves, which always come from Room.
    private val requestState = MutableStateFlow(PostListUiState(isLoading = true))

    val uiState: StateFlow<PostListUiState> = combine(observePosts(), requestState) { posts, state ->
        state.copy(posts = posts.toImmutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = requestState.value,
    )

    private val _uiEffect = Channel<PostListUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<PostListUiEffect> = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (isPostCacheEmpty()) {
                load()
            } else {
                // Show the cache right away and refresh quietly in the background.
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
                if (uiState.value.posts.isEmpty()) {
                    requestState.update { it.copy(error = result.error) }
                } else {
                    _uiEffect.send(PostListUiEffect.ShowError(result.error))
                }
            } else {
                requestState.update { it.copy(error = null) }
            }
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
    }
}
