package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.designsystem.base.BaseScreen
import com.merveylcu.ngamingcase.core.designsystem.component.refresh.NgamingPullToRefreshBox
import com.merveylcu.ngamingcase.core.designsystem.component.scaffold.NgamingScaffold
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListUiState

@Composable
internal fun PostListContent(
    state: BaseUiState<PostListUiState>,
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
        BaseScreen(
            uiState = state,
            modifier = Modifier.padding(innerPadding),
            onRetry = onRetry,
        ) { data ->
            NgamingPullToRefreshBox(
                isRefreshing = data.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (data.posts.isEmpty()) {
                    PostListEmptyContent()
                } else {
                    PostList(posts = data.posts, onPostClick = onPostClick, onDelete = onDelete)
                }
            }
        }
    }
}
