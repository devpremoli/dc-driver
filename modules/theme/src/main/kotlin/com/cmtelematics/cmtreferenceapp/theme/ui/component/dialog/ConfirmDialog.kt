package com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cmtelematics.cmtreferenceapp.theme.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton

@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    cancel: () -> Unit,
    confirm: () -> Unit,
    title: String,
    text: String,
    confirmButtonTitle: String = stringResource(R.string.confirm_dialog_button_yes),
    cancelButtonTitle: String = stringResource(R.string.confirm_dialog_button_no)
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = cancel,
            title = {
                Text(
                    text = title,
                    style = AppTheme.typography.title.large
                )
            },
            text = {
                Text(
                    text = text,
                    style = AppTheme.typography.text.large
                )
            },
            confirmButton = {
                TextButton(
                    onClick = confirm,
                    text = confirmButtonTitle
                )
            },
            dismissButton = {
                TextButton(
                    onClick = cancel,
                    text = cancelButtonTitle
                )
            },
            backgroundColor = AppTheme.colors.background.surface,
            contentColor = AppTheme.colors.text.body
        )
    }
}
