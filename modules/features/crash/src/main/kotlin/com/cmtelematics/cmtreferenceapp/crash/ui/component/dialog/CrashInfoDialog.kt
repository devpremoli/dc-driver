package com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog.InfoDialogType.REPORT_A_CLAIM
import com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog.InfoDialogType.REQUEST_A_TOW
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton

@Composable
internal fun CrashInfoDialog(
    infoDialogType: InfoDialogType? = null,
    title: String = stringResource(id = R.string.crash_info_dialog_default_title),
    text: String? = null,
    dismissButtonText: String = stringResource(id = R.string.error_dialog_dismiss_generic),
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = AppTheme.typography.title.large) },
        text = {
            Text(
                text = when (infoDialogType) {
                    REPORT_A_CLAIM -> stringResource(R.string.crash_info_dialog_report_a_claim_description)
                    REQUEST_A_TOW -> stringResource(R.string.crash_info_dialog_request_a_tow_description)
                    else -> text ?: error("You must define the text of description!")
                },
                style = AppTheme.typography.text.large
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                text = dismissButtonText
            )
        },
        backgroundColor = AppTheme.colors.background.surface,
        contentColor = AppTheme.colors.text.body
    )
}

internal enum class InfoDialogType {
    REPORT_A_CLAIM,
    REQUEST_A_TOW
}

@Preview
@Composable
private fun ErrorDialogContentPreview() {
    AppTheme {
        CrashInfoDialog(
            infoDialogType = REPORT_A_CLAIM,
            onDismiss = { }
        )
    }
}
