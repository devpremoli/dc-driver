package com.cmtelematics.cmtreferenceapp.trips.ui.component.marker

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Acceleration
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Braking
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Cornering
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Distraction
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Speeding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun EventMarker(
    icon: Bitmap,
    event: MapEvent,
    showEventInfo: (MapEvent) -> Unit
) {
    MarkerInfoWindowContent(
        state = MarkerState(position = event.eventPoint),
        icon = BitmapDescriptorFactory.fromBitmap(icon),
        flat = true,
        anchor = Offset(MARKER_ANCHOR_X_OFFSET, MARKER_ANCHOR_Y_OFFSET),
        infoWindowAnchor = Offset(INFO_WINDOW_ANCHOR_X_OFFSET, INFO_WINDOW_ANCHOR_Y_OFFSET),
        onInfoWindowClick = { marker ->
            showEventInfo(event)
            marker.hideInfoWindow()
        },
        content = { EventInfoCard(event) }
    )
}

@Composable
private fun EventInfoCard(event: MapEvent) {
    Row(
        modifier = Modifier.padding(AppTheme.dimens.margin.extraTiny),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
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
                ),
                style = AppTheme.typography.title.small
            )

            if (event is Distraction || event is Speeding) {
                Text(
                    text = when (event) {
                        is Distraction -> event.length.toComponents { minutes, seconds, _ ->
                            stringResource(
                                id = R.string.trip_details_distraction_maker_info_window_description,
                                minutes,
                                seconds
                            )
                        }
                        is Speeding -> stringResource(
                            id = R.string.trip_details_speeding_maker_info_window_description,
                            event.speedLimit.value.toInt(),
                            event.actualSpeed.value.toInt()
                        )
                        else -> error("Unsupported event type: ${event::class.java.name}")
                    },
                    style = AppTheme.typography.title.small,
                    color = AppTheme.colors.divider
                )
            }
        }
        Image(
            modifier = Modifier.padding(
                vertical = AppTheme.dimens.margin.default,
                horizontal = AppTheme.dimens.margin.small
            ),
            painter = painterResource(id = R.drawable.ic_marker_info),
            contentDescription = null
        )
    }
}

private const val INFO_WINDOW_ANCHOR_X_OFFSET = 0.5f
private const val INFO_WINDOW_ANCHOR_Y_OFFSET = 0.1f

private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
