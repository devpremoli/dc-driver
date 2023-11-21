package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    colors: AppColors = AppTheme.colors,
    dimens: AppDimens = AppTheme.dimens,
    typography: AppTypography = AppTypography(colors, dimens),
    shapes: AppShapes = AppTheme.shapes,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppDimens provides dimens,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes
    ) {
        MaterialTheme(colors = debugColors(isLight = colors.isLight)) {
            CompositionLocalProvider(
                LocalTextStyle provides AppTheme.typography.text.medium,
                LocalRippleTheme provides AppRippleTheme,
                LocalTextSelectionColors provides appTextSelectionColors(),
                LocalMinimumTouchTargetEnforcement provides false
            ) { content() }
        }
    }
}

object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val dimens: AppDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimens.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current
}
