package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes

@Composable
fun PostDetailScreen(
    postId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostDetailViewModel =
        hiltViewModel<PostDetailViewModel, PostDetailViewModel.Factory>(
            key = postId.toString(),
            creationCallback = { factory -> factory.create(postId) },
        ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val currentOnBack by rememberUpdatedState(onBack)

    LaunchedEffect(viewModel, snackbarHostState) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                PostDetailUiEffect.NavigateBack -> {
                    currentOnBack()
                }

                is PostDetailUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(resources.getString(effect.error.toMessageRes()))
                }
            }
        }
    }

    BackHandler(onBack = viewModel::onBack)

    PostDetailContent(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onBack = viewModel::onBack,
        onSave = viewModel::onSave,
        onTitleChange = viewModel::onTitleChange,
        onBodyChange = viewModel::onBodyChange,
        onDiscardConfirm = viewModel::onDiscardConfirm,
        onDiscardDismiss = viewModel::onDiscardDismiss,
        modifier = modifier,
    )
}
