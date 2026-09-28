package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.designsystem.base.BaseScreen
import com.merveylcu.ngamingcase.core.designsystem.component.scaffold.NgamingScaffold
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailUiState

@Composable
internal fun PostDetailContent(
    state: BaseUiState<PostDetailUiState>,
    snackBarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val content = (state as? BaseUiState.Content)?.data

    NgamingScaffold(
        snackBarHostState = snackBarHostState,
        modifier = modifier.fillMaxSize(),
        topBar = {
            PostDetailTopBar(
                isSaving = content?.isSaving == true,
                showSave = content?.post != null,
                canSave = content?.canSave == true,
                onBack = onBack,
                onSave = onSave,
            )
        },
    ) { innerPadding ->
        BaseScreen(uiState = state, modifier = Modifier.padding(innerPadding)) { data ->
            val post = data.post
            if (post == null) {
                PostDetailNotFoundContent(modifier = Modifier.fillMaxSize())
            } else {
                PostDetailForm(
                    imageUrl = post.imageUrl,
                    title = data.title,
                    body = data.body,
                    isTitleValid = data.isTitleValid,
                    enabled = !data.isSaving,
                    onTitleChange = onTitleChange,
                    onBodyChange = onBodyChange,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
