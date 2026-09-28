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
    spacing: NgamingCaseSpacing = NgamingCaseSpacing(),
    dimens: NgamingCaseDimens = NgamingCaseDimens(),
    typography: NgamingCaseTypography = DefaultTypography,
    shapes: NgamingCaseShapes = NgamingCaseShapes(),
    iconSizes: NgamingCaseIconSizes = NgamingCaseIconSizes(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val colorScheme = remember(colors) { colors.toColorScheme() }
    val materialTypography = remember(typography) { typography.toTypography() }
    val materialShapes = remember(shapes) { shapes.toShapes() }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalSpacing provides spacing,
        LocalDimens provides dimens,
        LocalTypography provides typography,
        LocalShapes provides shapes,
        LocalIconSizes provides iconSizes,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

object NgamingCaseTheme {
    val colors: NgamingCaseColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val spacing: NgamingCaseSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val dimens: NgamingCaseDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current

    val typography: NgamingCaseTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: NgamingCaseShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val iconSizes: NgamingCaseIconSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalIconSizes.current
}
