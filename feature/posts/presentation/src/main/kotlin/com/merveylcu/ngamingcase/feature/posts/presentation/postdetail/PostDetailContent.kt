package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose.PostDetailDiscardDialog
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose.PostDetailForm
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose.PostDetailNotFoundContent
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose.PostDetailTopBar

@Composable
internal fun PostDetailContent(
    state: PostDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDiscardConfirm: () -> Unit,
    onDiscardDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PostDetailTopBar(
                isSaving = state.isSaving,
                showSave = state.post != null,
                canSave = state.canSave,
                onBack = onBack,
                onSave = onSave,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.isLoading -> {
                Box(contentModifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.notFound || state.post == null -> {
                PostDetailNotFoundContent(modifier = contentModifier)
            }

            else -> {
                PostDetailForm(
                    imageUrl = state.post.imageUrl,
                    title = state.title,
                    body = state.body,
                    isTitleValid = state.isTitleValid,
                    enabled = !state.isSaving,
                    onTitleChange = onTitleChange,
                    onBodyChange = onBodyChange,
                    modifier = contentModifier,
                )
            }
        }
    }

    if (state.isDiscardDialogVisible) {
        PostDetailDiscardDialog(onConfirm = onDiscardConfirm, onDismiss = onDiscardDismiss)
    }
}

private val previewPost = Post(
    id = 1,
    title = "sunt aut facere repellat provident occaecati",
    body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
    imageUrl = "",
)

@Preview
@Composable
private fun PostDetailContentPreview() {
    NgamingCaseTheme {
        PostDetailContent(
            state = PostDetailUiState(
                post = previewPost,
                title = previewPost.title,
                body = previewPost.body,
                isLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
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
private fun PostDetailContentEmptyTitlePreview() {
    NgamingCaseTheme {
        PostDetailContent(
            state = PostDetailUiState(
                post = previewPost,
                title = "",
                body = previewPost.body,
                isLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
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
private fun PostDetailContentNotFoundPreview() {
    NgamingCaseTheme {
        PostDetailContent(
            state = PostDetailUiState(isLoading = false, notFound = true),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSave = {},
            onTitleChange = {},
            onBodyChange = {},
            onDiscardConfirm = {},
            onDiscardDismiss = {},
        )
    }
}
