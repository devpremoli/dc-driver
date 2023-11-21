package com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import kotlinx.coroutines.delay

@Composable
fun FocusedPlainTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLength: Int = Int.MAX_VALUE,
    focusDelay: FocusDelay = FocusDelay.Immediate
) {
    val focusRequester = remember { FocusRequester() }
    PlainTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        modifier = modifier.focusRequester(focusRequester),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLength = maxLength
    )
    LaunchedEffect(Unit) {
        delay(focusDelay.delayMs)
        focusRequester.requestFocus()
    }
}

@Suppress("MagicNumber")
enum class FocusDelay(val delayMs: Long) {
    Immediate(0), Short(300L)
}

@Preview(showBackground = true)
@Composable
private fun FocusedPlainTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            FocusedPlainTextField(
                value = "Filled",
                onValueChange = {},
                label = "Empty Label",
                placeholder = "Empty",
                keyboardOptions = KeyboardOptions()
            )
        }
    }
}
