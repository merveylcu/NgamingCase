package com.merveylcu.ngamingcase.core.designsystem.component.button

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@Composable
fun NgamingTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        NgamingText(text = text)
    }
}
