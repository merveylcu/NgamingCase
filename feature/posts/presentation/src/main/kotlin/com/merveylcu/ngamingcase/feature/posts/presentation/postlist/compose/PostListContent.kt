package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.refresh.NgamingPullToRefreshBox
import com.merveylcu.ngamingcase.core.designsystem.component.scaffold.NgamingScaffold
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListUiState

@Composable
internal fun PostListContent(
    state: PostListUiState,
    snackBarHostState: SnackbarHostState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onPostClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NgamingScaffold(
        snackBarHostState = snackBarHostState,
        modifier = modifier.fillMaxSize(),
        topBar = { PostListTopBar() },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.posts.isEmpty() && state.isLoading -> {
                Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                    NgamingProgressIndicator()
                }
            }

            state.posts.isEmpty() && state.error != null -> {
                PostListErrorContent(
                    error = state.error,
                    onRetry = onRetry,
                    modifier = contentModifier,
                )
            }

            else -> {
                NgamingPullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = contentModifier,
                ) {
                    if (state.posts.isEmpty()) {
                        PostListEmptyContent()
                    } else {
                        PostList(
                            posts = state.posts,
                            onPostClick = onPostClick,
                            onDelete = onDelete,
                        )
                    }
                }
            }
        }
    }
}
