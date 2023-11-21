package com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton

@Composable
fun ErrorDialog(
    title: String? = null,
    text: String? = null,
    dismissButtonText: String? = null,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = title?.let {
            {
                Text(text = it, style = AppTheme.typography.title.large)
            }
        },
        text = {
            Text(
                text = text ?: stringResource(id = R.string.error_dialog_generic),
                style = AppTheme.typography.text.large
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                text = dismissButtonText ?: stringResource(id = R.string.error_dialog_dismiss_generic)
            )
        },
        backgroundColor = AppTheme.colors.background.surface,
        contentColor = AppTheme.colors.text.body
    )
}

@Preview
@Composable
private fun ErrorDialogContentPreview() {
    AppTheme {
        ErrorDialog(
            title = "Title",
            onDismiss = { }
        )
    }
}
