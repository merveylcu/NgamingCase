package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListUiState
import kotlinx.collections.immutable.toImmutableList

private val previewPosts = List(5) { index ->
    Post(
        id = index + 1,
        title = "Post title ${index + 1}",
        body = "Short description of the post number ${index + 1}.",
        imageUrl = "",
    )
}.toImmutableList()

@Composable
private fun PostListContentPreview(state: BaseUiState<PostListUiState>) {
    NgamingCaseTheme {
        PostListContent(
            state = state,
            snackBarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onRetry = {},
            onPostClick = {},
            onDelete = {},
        )
    }
}

@Preview
@Composable
private fun PostListPreview() {
    PostListContentPreview(state = BaseUiState.Content(PostListUiState(posts = previewPosts)))
}

@Preview
@Composable
private fun PostListEmptyPreview() {
    PostListContentPreview(state = BaseUiState.Content(PostListUiState()))
}

@Preview
@Composable
private fun PostListLoadingPreview() {
    PostListContentPreview(state = BaseUiState.Loading)
}

@Preview
@Composable
private fun PostListErrorPreview() {
    PostListContentPreview(
        state = BaseUiState.Error(
            ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PostListItemPreview() {
    NgamingCaseTheme {
        Box {
            PostListItem(
                post = Post(
                    id = 1,
                    title = "sunt aut facere repellat provident occaecati excepturi optio",
                    body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
                    imageUrl = "",
                ),
                onClick = {},
            )
        }
    }
}
