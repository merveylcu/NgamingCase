package com.merveylcu.ngamingcase.core.designsystem.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingProgressIndicator

@Composable
fun NgamingLoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        NgamingProgressIndicator()
    }
}
