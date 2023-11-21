package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MinTapSize = 48.dp

@Immutable
data class AppDimens internal constructor(
    val minTapSize: Dp = MinTapSize,
    val margin: Margin = Margin(),
    val fontSize: FontSize = FontSize(),
    val lineHeight: LineHeight = LineHeight(),
    val button: Button = Button(),
    val iconSize: IconSize = IconSize()
) {
    data class Margin internal constructor(
        val enormous: Dp = 90.dp,
        val huge: Dp = 56.dp,
        val extraLarge: Dp = 48.dp,
        val larger: Dp = 44.dp,
        val large: Dp = 40.dp,
        val bigger: Dp = 32.dp,
        val bigRoomy: Dp = 28.dp,
        val big: Dp = 24.dp,
        val roomy: Dp = 20.dp,
        val default: Dp = 16.dp,
        val small: Dp = 12.dp,
        val smaller: Dp = 10.dp,
        val tiny: Dp = 8.dp,
        val tinier: Dp = 6.dp,
        val extraTiny: Dp = 4.dp,
        val tiniest: Dp = 2.dp
    )

    data class FontSize internal constructor(
        val titleXxl: TextUnit = 40.sp,
        val titleXl: TextUnit = 32.sp,
        val titleL: TextUnit = 24.sp,
        val titleM: TextUnit = 18.sp,
        val titleS: TextUnit = 16.sp,
        val textXl: TextUnit = 24.sp,
        val textL: TextUnit = 18.sp,
        val textM: TextUnit = 16.sp,
        val textS: TextUnit = 12.sp,
        val buttonL: TextUnit = 18.sp,
        val buttonTab: TextUnit = 12.sp
    )

    data class LineHeight internal constructor(
        val titleXxl: TextUnit = 48.sp,
        val titleXl: TextUnit = 38.sp,
        val titleL: TextUnit = 28.sp,
        val titleM: TextUnit = 22.sp,
        val titleS: TextUnit = 20.sp,
        val textXl: TextUnit = 28.sp,
        val textL: TextUnit = 22.sp,
        val textM: TextUnit = 20.sp,
        val textS: TextUnit = 16.sp,
        val buttonL: TextUnit = 22.sp,
        val buttonTab: TextUnit = 14.sp
    )

    data class IconSize internal constructor(
        val enormous: Dp = 120.dp,
        val huge: Dp = 80.dp,
        val large: Dp = 64.dp,
        val bigger: Dp = 48.dp,
        val big: Dp = 38.dp,
        val default: Dp = 24.dp,
        val small: Dp = 16.dp,
        val tiny: Dp = 12.dp
    )

    data class Button internal constructor(
        val minHeightLarge: Dp = 56.dp,
        val minHeightNormal: Dp = 42.dp,

        val horizontalPaddingLarge: Dp = 20.dp,
        val horizontalPaddingNormal: Dp = 16.dp,

        val verticalPaddingLarge: Dp = 18.dp,
        val verticalPaddingNormal: Dp = 11.dp,

        val defaultRadius: Dp = 12.dp
    )
}

internal val LocalAppDimens = staticCompositionLocalOf { AppDimens() }
