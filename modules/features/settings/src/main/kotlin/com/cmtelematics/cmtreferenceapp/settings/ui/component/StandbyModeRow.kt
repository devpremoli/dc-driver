package com.cmtelematics.cmtreferenceapp.settings.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.settings.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun StandbyModeRow(modifier: Modifier, standbyExpirationDate: ZonedDateTime?) {
    Column(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimens.margin.default,
                vertical = AppTheme.dimens.margin.tinier
            )
    ) {
        Text(
            text = stringResource(R.string.row_standby_mode),
            style = AppTheme.typography.text.large
        )

        val isStandByActive = standbyExpirationDate != null

        Text(
            text = if (isStandByActive) {
                val formattedDate = dateFormatter.format(standbyExpirationDate)
                val formattedTime = timeFormatter.format(standbyExpirationDate)
                stringResource(R.string.standby_mode_on, formattedDate, formattedTime)
            } else {
                stringResource(R.string.standby_mode_off)
            },
            color = if (isStandByActive) {
                AppTheme.colors.state.success
            } else {
                AppTheme.colors.state.danger
            }
        )
    }

    Divider(color = AppTheme.colors.divider)
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

@Preview(showBackground = true)
@Composable
private fun StandbyModeRowPreview() {
    AppTheme {
        StandbyModeRow(
            modifier = Modifier,
            standbyExpirationDate = ZonedDateTime.now()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StandbyModeRowStandbyModeOffPreview() {
    AppTheme {
        StandbyModeRow(
            modifier = Modifier,
            standbyExpirationDate = null
        )
    }
}
