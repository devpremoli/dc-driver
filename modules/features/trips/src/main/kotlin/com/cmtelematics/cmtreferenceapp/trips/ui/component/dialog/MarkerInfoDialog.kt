package com.cmtelematics.cmtreferenceapp.trips.ui.component.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.common.model.Mile
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Acceleration
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Braking
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Cornering
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Distraction
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Speeding
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.text.Typography.bullet
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun MarkerInfoDialog(
    showDialog: Boolean,
    event: MapEvent,
    cancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = cancel,
            title = { Title(event) },
            text = { Description(event) },
            confirmButton = {
                TextButton(
                    onClick = cancel,
                    text = stringResource(id = R.string.error_dialog_dismiss_generic)
                )
            },
            backgroundColor = AppTheme.colors.background.surface,
            contentColor = AppTheme.colors.text.body
        )
    }
}

@Composable
private fun Title(event: MapEvent) {
    Text(
        text = stringResource(
            id = when (event) {
                is Acceleration -> R.string.trip_details_acceleration_maker_title
                is Braking -> R.string.trip_details_braking_maker_title
                is Cornering -> R.string.trip_details_cornering_maker_title
                is Distraction -> R.string.trip_details_distraction_maker_title
                is Speeding -> R.string.trip_details_speeding_maker_title
            },
            timeFormatter.format(event.time)
        ).let { title ->
            if (event is Speeding) {
                title + stringResource(
                    id = R.string.trip_details_event_dialog_title_actual_speed,
                    event.actualSpeed.value.toInt()
                )
            } else {
                title
            }
        },
        style = AppTheme.typography.title.large
    )
}

@Composable
private fun Description(event: MapEvent) {
    if (event is Distraction) {
        DistractionDescription()
    } else {
        Text(
            text = stringResource(
                when (event) {
                    is Acceleration -> R.string.trip_details_event_dialog_acceleration_description
                    is Braking -> R.string.trip_details_event_dialog_braking_description
                    is Cornering -> R.string.trip_details_event_dialog_cornering_description
                    is Speeding -> R.string.trip_details_event_dialog_speeding_description
                    else -> error("Unsupported event type: ${event::class.java.name}")
                }
            ),
            style = AppTheme.typography.text.large
        )
    }
}

@Composable
private fun DistractionDescription() {
    val infoPoints = listOf(
        stringResource(R.string.trip_details_event_dialog_distraction_description_bulletpoint_1),
        stringResource(R.string.trip_details_event_dialog_distraction_description_bulletpoint_2),
        stringResource(R.string.trip_details_event_dialog_distraction_description_bulletpoint_3)
    )

    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.trip_details_event_dialog_distraction_description_part_1))
            infoPoints.forEach { infoPoint ->
                withStyle(style = ParagraphStyle()) {
                    append("\t")
                    append(bullet)
                    append("\t\t")
                    append(infoPoint)
                }
            }
            append("\n")
            append(stringResource(id = R.string.trip_details_event_dialog_distraction_description_part_2))
        }
    )
}

private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

@Suppress("MagicNumber")
@Preview
@Composable
private fun MarkerSpeedingInfoDialogPreview() {
    AppTheme {
        MarkerInfoDialog(
            showDialog = true,
            event = Speeding(
                eventPoint = LatLng(0.0, 0.0),
                time = LocalDateTime.now(),
                speedLimit = Mile(3.4),
                actualSpeed = Mile(22.3)
            ),
            cancel = { }
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun MarkerDistractionInfoDialogPreview() {
    AppTheme {
        MarkerInfoDialog(
            showDialog = true,
            event = Distraction(
                eventPoint = LatLng(0.0, 0.0),
                time = LocalDateTime.now(),
                length = 315.seconds
            ),
            cancel = { }
        )
    }
}
