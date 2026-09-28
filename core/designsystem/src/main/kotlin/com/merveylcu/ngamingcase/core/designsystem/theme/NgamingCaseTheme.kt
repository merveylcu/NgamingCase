package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun NgamingCaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dimens: Dimens = Dimens(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val colorScheme = remember(colors) { colors.toColorScheme() }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimens provides dimens,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = NgamingCaseTypography,
            content = content,
        )
    }
}

object NgamingCaseTheme {
    val colors: NgamingCaseColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val dimens: Dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current
}
