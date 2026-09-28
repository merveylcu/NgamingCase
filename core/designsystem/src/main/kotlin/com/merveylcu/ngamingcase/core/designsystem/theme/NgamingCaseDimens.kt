package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class NgamingCaseDimens(val imageSm: Dp = 56.dp, val imageLg: Dp = 120.dp)

internal val LocalDimens = staticCompositionLocalOf { NgamingCaseDimens() }
