package com.cmtelematics.cmtreferenceapp.trips.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.ContentColorRippleTheme
import com.cmtelematics.cmtreferenceapp.trips.R

@Composable
internal fun RecordingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides ContentColorRippleTheme) {
        Button(
            onClick = onClick,
            modifier = modifier.defaultMinSize(minHeight = AppTheme.dimens.button.minHeightLarge),
            shape = AppTheme.shapes.button,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colors.state.danger,
                contentColor = AppTheme.colors.button.contained.content,
                disabledBackgroundColor = AppTheme.colors.state.danger.copy(alpha = RECORDING_BUTTON_DISABLED_ALPHA),
                disabledContentColor = AppTheme.colors.button.contained.disabledContent
            ),
            contentPadding = PaddingValues(
                horizontal = AppTheme.dimens.button.horizontalPaddingLarge,
                vertical = AppTheme.dimens.button.verticalPaddingLarge
            ),
            enabled = enabled
        ) {
            Image(
                painter = painterResource(
                    id = if (isRecording) {
                        R.drawable.ic_stop_recording
                    } else {
                        R.drawable.ic_start_recording
                    }
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = if (enabled) {
                        AppTheme.colors.button.contained.content
                    } else {
                        AppTheme.colors.button.contained.disabledContent
                    }
                )
            )

            Spacer(modifier = Modifier.width(AppTheme.dimens.margin.tiny))

            CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.button.large.style) {
                Text(text = text)
            }
        }
    }
}

private const val RECORDING_BUTTON_DISABLED_ALPHA = 0.5f

@Preview
@Composable
private fun RecordButtonPreview() {
    AppTheme {
        RecordingButton(
            text = stringResource(R.string.button_manual_trip_recording_start),
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            isRecording = false
        )
    }
}

@Preview
@Composable
private fun RecordButtonRecordingInProgressPreview() {
    AppTheme {
        RecordingButton(
            text = "Recording 00:00:00",
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            isRecording = true
        )
    }
}

@Preview
@Composable
private fun RecordButtonDisabledPreview() {
    AppTheme {
        RecordingButton(
            text = stringResource(R.string.button_automatic_trip_recording_in_progress),
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            isRecording = true,
            enabled = false
        )
    }
}
