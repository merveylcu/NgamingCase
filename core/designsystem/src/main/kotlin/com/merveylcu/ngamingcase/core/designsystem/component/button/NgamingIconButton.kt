package com.merveylcu.ngamingcase.core.designsystem.component.button

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.merveylcu.ngamingcase.core.designsystem.component.icon.NgamingIcon

@Composable
fun NgamingIconButton(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        NgamingIcon(painter = painter, contentDescription = contentDescription)
    }
}
