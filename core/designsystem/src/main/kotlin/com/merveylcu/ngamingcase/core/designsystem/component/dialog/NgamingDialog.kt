package com.merveylcu.ngamingcase.core.designsystem.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingTextButton
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@Composable
fun NgamingDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { NgamingText(text = title) },
        text = { NgamingText(text = message) },
        confirmButton = { NgamingTextButton(text = confirmText, onClick = onConfirm) },
        dismissButton = { NgamingTextButton(text = dismissText, onClick = onDismiss) },
    )
}
