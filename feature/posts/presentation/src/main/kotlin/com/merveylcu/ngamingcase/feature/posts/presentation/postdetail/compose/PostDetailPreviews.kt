package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.ngamingcase.core.common.UiText
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.common.base.DialogState
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailUiState

private val previewPost = Post(
    id = 1,
    title = "sunt aut facere repellat provident occaecati",
    body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
    imageUrl = "",
)

private val previewContent = PostDetailUiState(
    post = previewPost,
    title = previewPost.title,
    body = previewPost.body,
)

@Composable
private fun PostDetailContentPreview(state: BaseUiState<PostDetailUiState>) {
    NgamingCaseTheme {
        PostDetailContent(
            state = state,
            snackBarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSave = {},
            onTitleChange = {},
            onBodyChange = {},
        )
    }
}

@Preview
@Composable
private fun PostDetailFormPreview() {
    PostDetailContentPreview(state = BaseUiState.Content(previewContent))
}

@Preview
@Composable
private fun PostDetailEmptyTitlePreview() {
    PostDetailContentPreview(state = BaseUiState.Content(previewContent.copy(title = "")))
}

@Preview
@Composable
private fun PostDetailNotFoundPreview() {
    PostDetailContentPreview(state = BaseUiState.Content(PostDetailUiState()))
}

@Preview
@Composable
private fun PostDetailDiscardDialogPreview() {
    PostDetailContentPreview(
        state = BaseUiState.Content(
            data = previewContent.copy(title = "Edited title"),
            dialogState = DialogState(
                title = UiText.DynamicString("Discard changes?"),
                message = UiText.DynamicString("Your unsaved changes will be lost."),
                confirmText = UiText.DynamicString("Discard"),
                dismissText = UiText.DynamicString("Keep editing"),
                onConfirm = {},
                onDismiss = {},
            ),
        ),
    )
}
