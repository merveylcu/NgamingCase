package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListUiState

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { PostListTopBar() },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.posts.isEmpty() && state.isLoading -> {
                Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
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
                PullToRefreshBox(
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
