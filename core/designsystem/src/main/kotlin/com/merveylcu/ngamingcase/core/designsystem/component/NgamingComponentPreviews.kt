package com.merveylcu.ngamingcase.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingButton
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingTextButton
import com.merveylcu.ngamingcase.core.designsystem.component.dialog.NgamingDialog
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingSmallProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText
import com.merveylcu.ngamingcase.core.designsystem.component.textfield.NgamingTextField
import com.merveylcu.ngamingcase.core.designsystem.component.topbar.NgamingTopBar
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme

@Preview(showBackground = true)
@Composable
private fun NgamingTopBarPreview() {
    NgamingCaseTheme {
        NgamingTopBar(
            title = "Edit post",
            onBack = {},
            actions = { NgamingTextButton(text = "Save", onClick = {}) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NgamingTextAndButtonsPreview() {
    NgamingCaseTheme {
        Column(
            modifier = Modifier.padding(NgamingCaseTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(NgamingCaseTheme.spacing.sm),
        ) {
            NgamingText(text = "Title", style = NgamingCaseTheme.typography.titleMedium)
            NgamingText(
                text = "Body text",
                style = NgamingCaseTheme.typography.bodyMedium,
                color = NgamingCaseTheme.colors.onSurfaceVariant,
            )
            NgamingButton(text = "Retry", onClick = {})
            NgamingTextButton(text = "Save", onClick = {}, enabled = false)
            NgamingProgressIndicator()
            NgamingSmallProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NgamingTextFieldPreview() {
    NgamingCaseTheme {
        Column(
            modifier = Modifier.padding(NgamingCaseTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(NgamingCaseTheme.spacing.sm),
        ) {
            NgamingTextField(value = "Post title", onValueChange = {}, label = "Title")
            NgamingTextField(value = "", onValueChange = {
            }, label = "Title", errorText = "Title can't be empty")
        }
    }
}

@Preview
@Composable
private fun NgamingDialogPreview() {
    NgamingCaseTheme {
        NgamingDialog(
            title = "Discard changes?",
            message = "Your unsaved changes will be lost.",
            confirmText = "Discard",
            onConfirm = {},
            dismissText = "Keep editing",
            onDismiss = {},
        )
    }
}
