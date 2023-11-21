package com.cmtelematics.cmtreferenceapp.settings.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.cmtelematics.cmtreferenceapp.settings.R
import com.cmtelematics.cmtreferenceapp.settings.model.StandbyMode
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton

@Composable
internal fun StandbyModeSelectorDialog(
    showDialog: Boolean,
    cancel: () -> Unit,
    selectStandbyMode: (StandbyMode) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = cancel) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = AppTheme.colors.background.primary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = AppTheme.colors.background.primary,
                            shape = AppTheme.shapes.dialog
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = AppTheme.dimens.margin.big,
                            vertical = AppTheme.dimens.margin.default
                        ),
                        text = stringResource(R.string.standby_options_title),
                        style = AppTheme.typography.title.large
                    )

                    val optionNames = stringArrayResource(id = R.array.standby_mode_options)
                    StandbyMode.values().forEach { standByMode ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectStandbyMode(standByMode) }
                                .padding(
                                    horizontal = AppTheme.dimens.margin.big,
                                    vertical = AppTheme.dimens.margin.small
                                ),
                            text = optionNames[standByMode.ordinal],
                            style = AppTheme.typography.text.large
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppTheme.dimens.margin.tinier,
                                vertical = AppTheme.dimens.margin.tiniest
                            )
                    ) {
                        TextButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = cancel,
                            text = stringResource(R.string.standby_options_cancel).uppercase()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun StandbyModeSelectorDialogPreview() {
    AppTheme {
        StandbyModeSelectorDialog(
            showDialog = true,
            cancel = {},
            selectStandbyMode = {}
        )
    }
}
