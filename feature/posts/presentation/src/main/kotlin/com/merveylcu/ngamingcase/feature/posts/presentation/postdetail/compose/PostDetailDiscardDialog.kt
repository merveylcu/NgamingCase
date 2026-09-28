package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.component.dialog.NgamingDialog
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@Composable
internal fun PostDetailDiscardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NgamingDialog(
        title = stringResource(R.string.post_detail_discard_title),
        message = stringResource(R.string.post_detail_discard_message),
        confirmText = stringResource(R.string.post_detail_discard_confirm),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.post_detail_discard_dismiss),
        onDismiss = onDismiss,
        modifier = modifier,
    )
}
