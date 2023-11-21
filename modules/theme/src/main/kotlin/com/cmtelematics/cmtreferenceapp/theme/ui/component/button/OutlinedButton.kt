package com.cmtelematics.cmtreferenceapp.theme.ui.component.button

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppColors
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import androidx.compose.material.OutlinedButton as MaterialOutlinedButton

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonSize: ButtonSize = ButtonSize.Medium,
    buttonColor: AppColors.ButtonColors = AppTheme.colors.button.outlined
) {
    val buttonStyle = buttonSizeStyle(buttonSize)

    val borderColor = if (enabled) {
        buttonColor.content
    } else {
        buttonColor.disabledContent
    }

    MaterialOutlinedButton(
        modifier = modifier.defaultMinSize(minHeight = buttonSizeToMinHeight(buttonSize)),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = buttonColor.background,
            contentColor = buttonColor.content,
            disabledContentColor = buttonColor.disabledContent
        ),
        shape = AppTheme.shapes.button,
        border = BorderStroke(width = 1.dp, color = borderColor),
        contentPadding = buttonSizeToPadding(buttonSize),
        elevation = null
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
private fun OutlinedButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            OutlinedButton(
                text = "enabled",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            OutlinedButton(
                text = "disabled",
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedButtonSizesPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            OutlinedButton(
                text = "large",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Large
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            OutlinedButton(
                text = "medium",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Medium
            )
        }
    }
}
