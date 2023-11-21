@file:Suppress("MagicNumber")

package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class AppColors(
    val primary: Color,
    val secondary: Color,
    val gray: Gray,
    val state: State,
    val ripple: Ripple,
    val text: Text,
    val button: Button,
    val textField: TextFieldColors,
    val background: Background,
    val divider: Color,
    val tabBar: TabBarColors,
    val isLight: Boolean,
    val lineSegment: LineSegmentColors
) {

    data class BlackAndWhite(
        val black: Color = Color(0xFF000000),
        val white: Color = Color(0xFFFFFFFF)
    )

    data class Gray(
        val grayLight: Color = Color(0xFFEBEFF3),
        val grayMedium: Color = Color(0xFFBFC5CB),
        val grayDark: Color = Color(0xFF6D7882)
    )

    data class State(
        val success: Color = Color(0xFF23863B),
        val danger: Color = Color(0xFFD70015)
    )

    data class Ripple(
        val primary: Color = Color(0x33144373),
        val onPrimary: Color = Color(0x33FFFFFF)
    )

    data class Text(
        val title: Color,
        val body: Color,
        val tableDetails: Color
    )

    data class Background(
        val primary: Color,
        val surface: Color,
        val content: Color,
        val progressBar: Color,
        val statusBar: Color
    )

    data class Button(
        val contained: ButtonColors,
        val outlined: ButtonColors,
        val text: ButtonColors,
        val switch: SwitchColors
    )

    data class ButtonColors(
        val background: Color,
        val content: Color,
        val disabledBackground: Color,
        val disabledContent: Color
    )

    data class TextFieldColors(
        val background: Color,
        val text: Color,
        val placeholder: Color,
        val indicator: Color
    )

    data class SwitchColors(
        val checkedThumb: Color,
        val checkedTrack: Color,
        val checkedTrackAlpha: Float,
        val uncheckedThumb: Color,
        val uncheckedTrack: Color,
        val uncheckedTrackAlpha: Float
    )

    data class TabBarColors(
        val unselectedColor: Color,
        val selectedColor: Color
    )

    data class LineSegmentColors(
        val default: Color,
        val speeding: Color,
        val distraction: Color,
        val estimated: Color
    )
}

internal val LocalAppColors = staticCompositionLocalOf { lightColors() }

fun lightColors(
    primary: Color = Color(0xFF144373),
    secondary: Color = Color(0xFF1C77C3),
    bw: AppColors.BlackAndWhite = AppColors.BlackAndWhite(),
    gray: AppColors.Gray = AppColors.Gray(),
    state: AppColors.State = AppColors.State(),
    ripple: AppColors.Ripple = AppColors.Ripple(),
    text: AppColors.Text = AppColors.Text(
        title = bw.black,
        body = bw.black,
        tableDetails = gray.grayDark
    ),
    button: AppColors.Button = AppColors.Button(
        contained = AppColors.ButtonColors(
            background = secondary,
            content = bw.white,
            disabledBackground = gray.grayLight,
            disabledContent = gray.grayMedium
        ),
        outlined = AppColors.ButtonColors(
            background = Color.Transparent,
            content = secondary,
            disabledBackground = Color.Transparent,
            disabledContent = gray.grayMedium
        ),
        text = AppColors.ButtonColors(
            background = Color.Transparent,
            content = secondary,
            disabledBackground = Color.Transparent,
            disabledContent = gray.grayMedium
        ),
        switch = AppColors.SwitchColors(
            checkedThumb = secondary,
            checkedTrack = secondary,
            checkedTrackAlpha = 0.38f,
            uncheckedThumb = bw.white,
            uncheckedTrack = gray.grayMedium,
            uncheckedTrackAlpha = 1f
        )
    ),
    textField: AppColors.TextFieldColors = AppColors.TextFieldColors(
        background = bw.white,
        text = bw.black,
        placeholder = gray.grayMedium,
        indicator = gray.grayMedium
    ),
    background: AppColors.Background = AppColors.Background(
        primary = bw.white,
        surface = bw.white,
        content = gray.grayLight,
        progressBar = primary,
        statusBar = primary
    )
) = AppColors(
    primary = primary,
    secondary = secondary,
    gray = gray,
    state = state,
    ripple = ripple,
    text = text,
    button = button,
    textField = textField,
    background = background,
    divider = gray.grayMedium,
    tabBar = AppColors.TabBarColors(
        unselectedColor = gray.grayDark,
        selectedColor = secondary
    ),
    isLight = true,
    lineSegment = AppColors.LineSegmentColors(
        default = Color.Green,
        speeding = Color.Red,
        distraction = Color.Blue,
        estimated = Color.DarkGray
    )
)

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [androidx.compose.material.MaterialTheme.colors] in preference to [AppTheme.colors].
 */
fun debugColors(
    debugColor: Color = Color.Magenta,
    isLight: Boolean = true
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = isLight
)

@Suppress("LongMethod", "StringLiteralDuplication", "MaxChainedCallsOnSameLine")
@Preview(showBackground = true, widthDp = 880, heightDp = 1700)
@Composable
private fun AppColorsPreview() {
    AppTheme {
        Column(Modifier.padding(AppTheme.dimens.margin.default)) {
            ColorPreviewRow(name = "Main") {
                ColorPreview("Primary", AppTheme.colors.primary)
                ColorPreview("Secondary", AppTheme.colors.secondary)
                ColorPreview("Divider", AppTheme.colors.divider)
            }

            ColorPreviewRow(name = "State") {
                ColorPreview("Success", AppTheme.colors.state.success)
                ColorPreview("Danger", AppTheme.colors.state.danger)
            }

            ColorPreviewRow(name = "Ripple") {
                ColorPreview("Primary", AppTheme.colors.ripple.primary)
                ColorPreview("onPrimary", AppTheme.colors.ripple.onPrimary)
            }

            ColorPreviewRow(name = "Text") {
                ColorPreview(name = "Title", color = AppTheme.colors.text.title)
                ColorPreview(name = "Body", color = AppTheme.colors.text.body)
            }

            ColorPreviewRow(name = "Button - Outlined") {
                ColorPreview(name = "Content", color = AppTheme.colors.button.outlined.content)
                ColorPreview(name = "Background", color = AppTheme.colors.button.outlined.background)
                ColorPreview(name = "DisabledContent", color = AppTheme.colors.button.outlined.disabledContent)
                ColorPreview(name = "DisabledBackground", color = AppTheme.colors.button.outlined.disabledBackground)
            }

            ColorPreviewRow(name = "Button - Text") {
                ColorPreview(name = "Content", color = AppTheme.colors.button.text.content)
                ColorPreview(name = "Background", color = AppTheme.colors.button.text.background)
                ColorPreview(name = "DisabledContent", color = AppTheme.colors.button.text.disabledContent)
                ColorPreview(name = "DisabledBackground", color = AppTheme.colors.button.text.disabledBackground)
            }

            ColorPreviewRow(name = "Button - Contained") {
                ColorPreview(name = "Content", color = AppTheme.colors.button.contained.content)
                ColorPreview(name = "Background", color = AppTheme.colors.button.contained.background)
                ColorPreview(name = "DisabledContent", color = AppTheme.colors.button.contained.disabledContent)
                ColorPreview(name = "DisabledBackground", color = AppTheme.colors.button.contained.disabledBackground)
            }

            ColorPreviewRow(name = "Switch") {
                ColorPreview(name = "CheckedThumb", color = AppTheme.colors.button.switch.checkedThumb)
                ColorPreview(
                    name = "CheckedTrack",
                    color = AppTheme.colors.button.switch.checkedTrack.copy(
                        alpha = AppTheme.colors.button.switch.checkedTrackAlpha
                    )
                )

                ColorPreview(name = "UncheckedThumb", color = AppTheme.colors.button.switch.uncheckedThumb)
                ColorPreview(
                    name = "UncheckedTrack",
                    color = AppTheme.colors.button.switch.uncheckedThumb.copy(
                        alpha = AppTheme.colors.button.switch.uncheckedTrackAlpha
                    )
                )
            }

            ColorPreviewRow(name = "Text Field") {
                ColorPreview(name = "Text", color = AppTheme.colors.textField.text)
                ColorPreview(name = "Indicator", color = AppTheme.colors.textField.indicator)
                ColorPreview(name = "Background", color = AppTheme.colors.textField.background)
                ColorPreview(name = "Placeholder", color = AppTheme.colors.textField.placeholder)
            }

            ColorPreviewRow(name = "Background") {
                ColorPreview(name = "Surface", color = AppTheme.colors.background.surface)
                ColorPreview(name = "Content", color = AppTheme.colors.background.content)
                ColorPreview(name = "Status bar", color = AppTheme.colors.background.statusBar)
                ColorPreview(name = "Progress bar", color = AppTheme.colors.background.progressBar)
                ColorPreview(name = "Primary", color = AppTheme.colors.background.primary)
            }

            ColorPreviewRow(name = "Line segment") {
                ColorPreview(name = "Default", color = AppTheme.colors.lineSegment.default)
                ColorPreview(name = "Speeding", color = AppTheme.colors.lineSegment.speeding)
                ColorPreview(name = "Distraction", color = AppTheme.colors.lineSegment.distraction)
                ColorPreview(name = "Estimated", color = AppTheme.colors.lineSegment.estimated)
            }
        }
    }
}

@Composable
private fun ColorPreviewRow(name: String, content: @Composable RowScope.() -> Unit) {
    Text(text = name, style = AppTheme.typography.title.large)

    Row(content = content)
}

@Composable
private fun ColorPreview(name: String, color: Color) {
    Column(Modifier.padding(end = AppTheme.dimens.margin.tiny)) {
        Box(
            Modifier
                .size(80.dp, 80.dp)
                .border(1.dp, Color.Black)
                .background(color)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.tiny))

        Text(
            text = name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = AppTheme.typography.text.medium
        )
    }
}
