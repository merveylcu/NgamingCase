package com.merveylcu.ngamingcase.core.designsystem.component.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme

@Composable
fun NgamingProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun NgamingSmallProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(NgamingCaseTheme.iconSizes.medium),
        strokeWidth = NgamingCaseTheme.dimens.progressStrokeWidth,
    )
}
