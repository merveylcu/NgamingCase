package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostListContent
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun PostListScreen(onPostClick: (Int) -> Unit, viewModel: PostListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current

    LaunchedEffect(viewModel, snackBarHostState) {
        viewModel.uiEffect.collect { effect ->
            snackBarHostState.currentSnackbarData?.dismiss()
            when (effect) {
                is PostListUiEffect.ShowUndoDelete -> launch {
                    showUndoDeleteSnackBar(
                        snackBarHostState = snackBarHostState,
                        message = resources.getString(R.string.post_list_deleted),
                        actionLabel = resources.getString(R.string.post_list_undo),
                        onUndo = { viewModel.onUndoDelete(effect.postId) },
                        onConfirm = { viewModel.onDeleteConfirm(effect.postId) },
                    )
                }

                is PostListUiEffect.ShowError -> launch {
                    snackBarHostState.showSnackbar(resources.getString(effect.error.toMessageRes()))
                }
            }
        }
    }

    PostListContent(
        state = uiState,
        snackBarHostState = snackBarHostState,
        onRefresh = viewModel::onRefresh,
        onRetry = viewModel::onRetry,
        onPostClick = onPostClick,
        onDelete = viewModel::onDelete,
    )
}

private suspend fun showUndoDeleteSnackBar(
    snackBarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    onUndo: () -> Unit,
    onConfirm: () -> Unit,
) {
    val result = try {
        snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short,
        )
    } catch (e: CancellationException) {
        onConfirm()
        throw e
    }
    when (result) {
        SnackbarResult.ActionPerformed -> onUndo()
        SnackbarResult.Dismissed -> onConfirm()
    }
}
