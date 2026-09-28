package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.scaffold.NgamingScaffold
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailUiState

@Composable
internal fun PostDetailContent(
    state: PostDetailUiState,
    snackBarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDiscardConfirm: () -> Unit,
    onDiscardDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NgamingScaffold(
        snackBarHostState = snackBarHostState,
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
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.isLoading -> {
                Box(contentModifier, contentAlignment = Alignment.Center) {
                    NgamingProgressIndicator()
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
