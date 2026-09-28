package com.merveylcu.ngamingcase.feature.posts.presentation.compose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme

@Composable
internal fun PostImage(
    url: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val placeholderColor = NgamingCaseTheme.colors.surfaceVariant
    val placeholder = remember(placeholderColor) { ColorPainter(placeholderColor) }

    AsyncImage(
        model = url,
        contentDescription = null,
        placeholder = placeholder,
        error = placeholder,
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .size(size)
                .clip(NgamingCaseTheme.shapes.full),
    )
}
