package com.merveylcu.ngamingcase.core.designsystem.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingTextButton
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@Composable
fun NgamingDialog(
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissText: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = title?.let { { NgamingText(text = it) } },
        text = { NgamingText(text = message) },
        confirmButton = { NgamingTextButton(text = confirmText, onClick = onConfirm) },
        dismissButton = dismissText?.let { { NgamingTextButton(text = it, onClick = onDismiss) } },
    )
}
