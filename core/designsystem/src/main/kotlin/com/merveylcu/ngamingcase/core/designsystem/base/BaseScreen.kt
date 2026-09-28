package com.merveylcu.ngamingcase.core.designsystem.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.designsystem.component.dialog.NgamingDialog
import com.merveylcu.ngamingcase.core.designsystem.component.dialog.NgamingLoadingDialog
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.state.NgamingErrorContent
import com.merveylcu.ngamingcase.core.designsystem.extension.asString
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes

@Composable
fun <S> BaseScreen(
    uiState: BaseUiState<S>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    content: @Composable (S) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            BaseUiState.Loading -> {
                NgamingProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is BaseUiState.Error -> {
                NgamingErrorContent(
                    message = stringResource(uiState.error.toMessageRes()),
                    onRetry = onRetry,
                )
            }

            is BaseUiState.Content -> {
                content(uiState.data)
                if (uiState.isLoading) {
                    NgamingLoadingDialog()
                }
                uiState.dialogState?.let { dialog ->
                    NgamingDialog(
                        message = dialog.message.asString(),
                        confirmText = dialog.confirmText.asString(),
                        onConfirm = dialog.onConfirm,
                        onDismiss = dialog.onDismiss,
                        title = dialog.title?.asString(),
                        dismissText = dialog.dismissText?.asString(),
                    )
                }
            }
        }
    }
}
