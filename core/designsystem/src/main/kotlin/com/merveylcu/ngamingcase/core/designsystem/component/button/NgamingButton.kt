package com.merveylcu.ngamingcase.core.designsystem.component.button

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@Composable
fun NgamingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(onClick = onClick, modifier = modifier, enabled = enabled) {
        NgamingText(text = text)
    }
}
