package com.cmtelematics.cmtreferenceapp.theme.ui.component.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.ContentColorRippleTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonSize: ButtonSize = ButtonSize.Medium
) {
    ContainedButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        buttonSize = buttonSize
    )
}

@Composable
fun ContainedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonSize: ButtonSize = ButtonSize.Large,
    colors: ButtonColors? = null,
    shape: Shape = AppTheme.shapes.button
) {
    val buttonStyle = buttonSizeStyle(buttonSize)

    CompositionLocalProvider(LocalRippleTheme provides ContentColorRippleTheme) {
        Button(
            onClick = onClick,
            modifier = modifier.defaultMinSize(minHeight = buttonSizeToMinHeight(buttonSize)),
            enabled = enabled,
            shape = shape,
            elevation = null,
            colors = colors ?: ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colors.button.contained.background,
                contentColor = AppTheme.colors.button.contained.content,
                disabledBackgroundColor = AppTheme.colors.button.contained.disabledBackground,
                disabledContentColor = AppTheme.colors.button.contained.disabledContent
            ),
            contentPadding = buttonSizeToPadding(buttonSize)
        ) {
            val actualText = if (buttonStyle.textAllCaps) {
                text.uppercase()
            } else {
                text
            }

            CompositionLocalProvider(LocalTextStyle provides buttonStyle.style) {
                Text(text = actualText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            PrimaryButton(
                text = "enabled",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            PrimaryButton(
                text = "disabled",
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonSizesPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            PrimaryButton(
                text = "large",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Large
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            PrimaryButton(
                text = "medium",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Medium
            )
        }
    }
}
