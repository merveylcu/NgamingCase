package com.merveylcu.ngamingcase.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val LightColorScheme = lightColorScheme(
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

internal val DarkColorScheme = darkColorScheme(
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
