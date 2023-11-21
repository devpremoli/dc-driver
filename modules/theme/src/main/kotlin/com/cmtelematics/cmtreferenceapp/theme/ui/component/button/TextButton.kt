package com.cmtelematics.cmtreferenceapp.theme.ui.component.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import androidx.compose.material.TextButton as MaterialTextButton

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonSize: ButtonSize = ButtonSize.Medium
) {
    val buttonStyle = buttonSizeStyle(buttonSize)

    MaterialTextButton(
        modifier = modifier.defaultMinSize(minHeight = buttonSizeToMinHeight(buttonSize)),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.button.text.background,
            contentColor = AppTheme.colors.button.text.content,
            disabledBackgroundColor = AppTheme.colors.button.text.disabledBackground,
            disabledContentColor = AppTheme.colors.button.text.disabledContent
        ),
        contentPadding = buttonSizeToPadding(buttonSize),
        shape = AppTheme.shapes.button
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

@Preview(showBackground = true)
@Composable
private fun TextButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            TextButton(text = "enabled", {})

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            TextButton(
                text = "disabled",
                onClick = {},
                enabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextButtonSizesPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            TextButton(
                text = "large",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            TextButton(
                text = "medium",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Medium
            )
        }
    }
}
