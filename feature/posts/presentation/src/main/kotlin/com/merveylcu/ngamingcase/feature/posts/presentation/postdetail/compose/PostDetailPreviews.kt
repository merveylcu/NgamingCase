package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailUiState

private val previewPost = Post(
    id = 1,
    title = "sunt aut facere repellat provident occaecati",
    body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
    imageUrl = "",
)

@Composable
private fun PostDetailContentPreview(state: PostDetailUiState) {
    NgamingCaseTheme {
        PostDetailContent(
            state = state,
            snackBarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSave = {},
            onTitleChange = {},
            onBodyChange = {},
            onDiscardConfirm = {},
            onDiscardDismiss = {},
        )
    }
}

@Preview
@Composable
private fun PostDetailFormPreview() {
    PostDetailContentPreview(
        state = PostDetailUiState(
            post = previewPost,
            title = previewPost.title,
            body = previewPost.body,
            isLoading = false,
        ),
    )
}

@Preview
@Composable
private fun PostDetailEmptyTitlePreview() {
    PostDetailContentPreview(
        state = PostDetailUiState(
            post = previewPost,
            title = "",
            body = previewPost.body,
            isLoading = false,
        ),
    )
}

@Preview
@Composable
private fun PostDetailNotFoundPreview() {
    PostDetailContentPreview(
        state = PostDetailUiState(
            isLoading = false,
            notFound = true,
        ),
    )
}
