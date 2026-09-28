package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class NgamingCaseDimens(
    val spacingXs: Dp = 4.dp,
    val spacingSm: Dp = 8.dp,
    val spacingMd: Dp = 16.dp,
    val spacingLg: Dp = 24.dp,
    val spacingXl: Dp = 32.dp,
    val imageSm: Dp = 56.dp,
    val imageLg: Dp = 120.dp,
    val progressStrokeWidth: Dp = 2.dp,
)

internal val LocalDimens = staticCompositionLocalOf { NgamingCaseDimens() }
