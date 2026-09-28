package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun NgamingCaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dimens: Dimens = Dimens(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDimens provides dimens) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = NgamingCaseTypography,
            content = content,
        )
    }
}

object NgamingCaseTheme {
    val dimens: Dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current
}
