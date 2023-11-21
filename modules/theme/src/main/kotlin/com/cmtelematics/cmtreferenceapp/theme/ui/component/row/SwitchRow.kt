package com.cmtelematics.cmtreferenceapp.theme.ui.component.row

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider

@Composable
fun SwitchRow(
    text: String,
    checked: Boolean,
    checkedChanged: (Boolean) -> Unit,
    addPadding: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.minTapSize)
                .padding(horizontal = if (addPadding) AppTheme.dimens.margin.default else 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = AppTheme.typography.text.large
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = checked,
                onCheckedChange = checkedChanged,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppTheme.colors.button.switch.checkedThumb,
                    checkedTrackColor = AppTheme.colors.button.switch.checkedTrack,
                    checkedTrackAlpha = AppTheme.colors.button.switch.checkedTrackAlpha,
                    uncheckedThumbColor = AppTheme.colors.button.switch.uncheckedThumb,
                    uncheckedTrackColor = AppTheme.colors.button.switch.uncheckedTrack,
                    uncheckedTrackAlpha = AppTheme.colors.button.switch.uncheckedTrackAlpha
                )
            )
        }
        Divider(
            color = AppTheme.colors.divider
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitchRowPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            SwitchRow(text = "Example", checked = false, checkedChanged = {})
            Spacer(Modifier.height(AppTheme.dimens.margin.default))
            SwitchRow(text = "Example", checked = true, checkedChanged = {})
        }
    }
}
