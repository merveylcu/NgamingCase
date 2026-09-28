package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class NgamingCaseColors(
    val isDark: Boolean,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
)

internal val LightColors = NgamingCaseColors(
    isDark = false,
    primary = Indigo40,
    onPrimary = Slate99,
    primaryContainer = Indigo90,
    onPrimaryContainer = Indigo10,
    background = Slate99,
    onBackground = Slate10,
    surface = Slate99,
    onSurface = Slate10,
    surfaceVariant = Slate90,
    onSurfaceVariant = Slate30,
    outline = Slate50,
    outlineVariant = Slate80,
    error = Red40,
    onError = Slate99,
    errorContainer = Red90,
    onErrorContainer = Red10,
)

internal val DarkColors = NgamingCaseColors(
    isDark = true,
    primary = Indigo80,
    onPrimary = Indigo20,
    primaryContainer = Indigo30,
    onPrimaryContainer = Indigo90,
    background = Slate10,
    onBackground = Slate90,
    surface = Slate10,
    onSurface = Slate90,
    surfaceVariant = Slate30,
    onSurfaceVariant = Slate80,
    outline = Slate50,
    outlineVariant = Slate30,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
)

internal val LocalColors = staticCompositionLocalOf { LightColors }

internal fun NgamingCaseColors.toColorScheme(): ColorScheme {
    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
    )
}
