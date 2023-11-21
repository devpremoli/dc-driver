package com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun SubmittableFocusedPlainTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    submitEnabled: Boolean,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    newLineOnImeActionAllowed: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    focusDelay: FocusDelay = FocusDelay.Immediate
) {
    FocusedPlainTextField(
        value = value,
        onValueChange = { text ->
            onValueChange(
                if (newLineOnImeActionAllowed) {
                    text
                } else {
                    text.replace("\n", "")
                }
            )
        },
        label = label,
        placeholder = placeholder,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (submitEnabled) {
                ImeAction.Done
            } else {
                ImeAction.None
            }
        ),
        keyboardActions = KeyboardActions(onDone = {
            if (submitEnabled) {
                submit()
            }
        }),
        maxLength = maxLength,
        focusDelay = focusDelay
    )
}

@Preview(showBackground = true)
@Composable
private fun SubmittableFocusedPlainTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            SubmittableFocusedPlainTextField(
                value = "Filled",
                onValueChange = {},
                label = "Empty Label",
                placeholder = "Empty",
                submitEnabled = true,
                submit = { }
            )
        }
    }
}
