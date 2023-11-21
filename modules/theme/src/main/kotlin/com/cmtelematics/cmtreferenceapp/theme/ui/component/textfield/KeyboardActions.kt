package com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun moveFocusToNextView(): KeyboardActions {
    val focusManager = LocalFocusManager.current
    return KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
}
