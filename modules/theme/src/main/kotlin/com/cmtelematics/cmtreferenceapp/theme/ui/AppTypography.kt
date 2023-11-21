@file:Suppress("MagicNumber")

package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    val title: Title,
    val text: Text,
    val button: Button
) {

    constructor(
        colors: AppColors,
        dimens: AppDimens,
        textAppearance: TextAppearance = TextAppearance(dimens)
    ) : this(
        title = Title(
            small = textAppearance.titleS.copy(color = colors.text.title),
            medium = textAppearance.titleM.copy(color = colors.text.title),
            large = textAppearance.titleL.copy(color = colors.text.title),
            xLarge = textAppearance.titleXl.copy(color = colors.text.title),
            xxLarge = textAppearance.titleXxl.copy(color = colors.text.title)
        ),
        text = Text(
            small = textAppearance.textS.copy(color = colors.text.body),
            medium = textAppearance.textM.copy(color = colors.text.body),
            large = textAppearance.textL.copy(color = colors.text.body),
            xLarge = textAppearance.textXl.copy(color = colors.text.body)
        ),
        button = Button(
            large = Button.Style(
                style = textAppearance.buttonL,
                textAllCaps = false
            ),
            tab = Button.Style(
                style = textAppearance.buttonTab,
                textAllCaps = false
            )
        )
    )

    data class Title(
        val small: TextStyle,
        val medium: TextStyle,
        val large: TextStyle,
        val xLarge: TextStyle,
        val xxLarge: TextStyle
    )

    data class Text(
        val small: TextStyle,
        val medium: TextStyle,
        val large: TextStyle,
        val xLarge: TextStyle
    )

    data class Button(
        val tab: Style,
        val large: Style
    ) {

        data class Style(
            val style: TextStyle,
            val textAllCaps: Boolean
        )
    }

    data class TextAppearance(
        val dimens: AppDimens,
        val titleXxl: TextStyle = TextStyle(
            fontSize = dimens.fontSize.titleXxl,
            fontWeight = FontWeight.Bold,
            lineHeight = dimens.lineHeight.titleXxl,
            letterSpacing = 0.sp
        ),
        val titleXl: TextStyle = TextStyle(
            fontSize = dimens.fontSize.titleXl,
            fontWeight = FontWeight.Bold,
            lineHeight = dimens.lineHeight.titleXl,
            letterSpacing = 0.sp
        ),
        val titleL: TextStyle = TextStyle(
            fontSize = dimens.fontSize.titleL,
            fontWeight = FontWeight.Bold,
            lineHeight = dimens.lineHeight.titleL,
            letterSpacing = 0.sp
        ),
        val titleM: TextStyle = TextStyle(
            fontSize = dimens.fontSize.titleM,
            fontWeight = FontWeight.Bold,
            lineHeight = dimens.lineHeight.titleM,
            letterSpacing = 0.15.sp
        ),
        val titleS: TextStyle = TextStyle(
            fontSize = dimens.fontSize.titleS,
            fontWeight = FontWeight.Bold,
            lineHeight = dimens.lineHeight.titleS,
            letterSpacing = 0.15.sp
        ),
        val textXl: TextStyle = TextStyle(
            fontSize = dimens.fontSize.textXl,
            lineHeight = dimens.lineHeight.textXl,
            letterSpacing = 0.5.sp
        ),
        val textL: TextStyle = TextStyle(
            fontSize = dimens.fontSize.textL,
            lineHeight = dimens.lineHeight.textL,
            letterSpacing = 0.5.sp
        ),
        val textM: TextStyle = TextStyle(
            fontSize = dimens.fontSize.textM,
            lineHeight = dimens.lineHeight.textM,
            letterSpacing = 0.5.sp
        ),
        val textS: TextStyle = TextStyle(
            fontSize = dimens.fontSize.textS,
            lineHeight = dimens.lineHeight.textS,
            letterSpacing = 0.5.sp
        ),
        val buttonL: TextStyle = TextStyle(
            fontSize = dimens.fontSize.buttonL,
            lineHeight = dimens.lineHeight.buttonL,
            letterSpacing = 0.4.sp,
            fontWeight = FontWeight.Medium
        ),
        val buttonTab: TextStyle = TextStyle(
            fontSize = dimens.fontSize.buttonTab,
            lineHeight = dimens.lineHeight.buttonTab,
            letterSpacing = 0.4.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

internal val LocalAppTypography =
    staticCompositionLocalOf<AppTypography> { error("No AppTypography provided") }

@Preview(showBackground = true)
@Composable
private fun AppTypographyPreview() {
    AppTheme {
        Column {
            Text("Title XXL", style = AppTheme.typography.title.xxLarge)
            Text("Title XL", style = AppTheme.typography.title.xLarge)
            Text("Title L", style = AppTheme.typography.title.large)
            Text("Title M", style = AppTheme.typography.title.medium)
            Text("Title S", style = AppTheme.typography.title.small)
            Text("Text XL", style = AppTheme.typography.text.xLarge)
            Text("Text L", style = AppTheme.typography.text.large)
            Text("Text M", style = AppTheme.typography.text.medium)
            Text("Text S", style = AppTheme.typography.text.small)
        }
    }
}
