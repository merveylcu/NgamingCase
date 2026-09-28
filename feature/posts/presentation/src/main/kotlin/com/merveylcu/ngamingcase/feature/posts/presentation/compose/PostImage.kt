package com.merveylcu.ngamingcase.feature.posts.presentation.compose

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage

@Composable
internal fun PostImage(
    url: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val placeholderColor = MaterialTheme.colorScheme.surfaceVariant
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
                .clip(CircleShape),
    )
}
