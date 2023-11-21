package com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Suppress("LongMethod")
@Composable
fun PlainTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLength: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
) {
    // Custom styling
    val colors = textFieldColors(
        cursorColor = AppTheme.colors.primary,
        textColor = AppTheme.colors.textField.text,
        placeholderColor = AppTheme.colors.textField.placeholder,
        backgroundColor = AppTheme.colors.textField.background,
        unfocusedIndicatorColor = AppTheme.colors.textField.indicator,
        focusedIndicatorColor = AppTheme.colors.textField.indicator,
        disabledIndicatorColor = AppTheme.colors.textField.indicator,
        errorIndicatorColor = AppTheme.colors.textField.indicator
    )
    val textColor = colors.textColor(enabled).value
    val mergedTextStyle = AppTheme.typography.text.medium.merge(TextStyle(color = textColor))

    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    // code copied from TextField() implementation with the padding tweaked
    @OptIn(ExperimentalMaterialApi::class)
    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue: TextFieldValue ->
            textFieldValueState = newValue

            if (value != newValue.text) {
                onValueChange(newValue.text.take(maxLength))
            }
        },
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, isError, interactionSource, colors)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = AppTheme.dimens.button.minHeightNormal
            ),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.textField.background)
            ) {
                label?.let {
                    LabelSlot(content = { Text(text = it) })
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    placeholder?.let {
                        HintSlot(
                            textFieldValue = textFieldValue.text,
                            content = { Text(text = it, color = AppTheme.colors.textField.placeholder) }
                        )
                    }

                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun LabelSlot(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Medium
        ),
        LocalContentColor provides AppTheme.colors.secondary
    ) { content() }
}

@Composable
private fun HintSlot(
    textFieldValue: String,
    content: @Composable () -> Unit
) {
    if (textFieldValue.isEmpty()) {
        CompositionLocalProvider(
            LocalContentColor provides AppTheme.colors.textField.placeholder
        ) { content() }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlainTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            PlainTextField(
                value = "",
                onValueChange = {},
                label = "Empty Label",
                placeholder = "Empty"
            )

            Spacer(Modifier.height(AppTheme.dimens.margin.default))

            PlainTextField(
                value = "Filled",
                onValueChange = {},
                label = "Label"
            )
        }
    }
}
