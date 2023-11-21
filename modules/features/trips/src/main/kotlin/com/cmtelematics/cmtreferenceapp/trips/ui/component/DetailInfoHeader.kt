package com.cmtelematics.cmtreferenceapp.trips.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.common.model.KiloMeter
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.UserTransportationMode
import com.cmtelematics.sdk.types.UserTransportationMode.DRIVER
import com.cmtelematics.sdk.types.UserTransportationMode.PASSENGER
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun DetailInfoHeader(
    modifier: Modifier = Modifier,
    trip: ProcessedTripSummary,
    transportationMode: UserTransportationMode,
    selectTransportationMode: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        InfoCard(trip)
        Spacer(modifier = Modifier.width(AppTheme.dimens.margin.smaller))
        TripTransportationModeCard(
            transportationMode = transportationMode,
            selectTransportationMode = selectTransportationMode
        )
    }
}

@Composable
private fun RowScope.InfoCard(trip: ProcessedTripSummary) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(TRIP_DETAIL_INFO_CARD_ROUNDED_CORNER))
            .background(AppTheme.colors.background.primary)
            .padding(AppTheme.dimens.margin.default)
            .height(intrinsicSize = IntrinsicSize.Max)
            .weight(1f)
    ) {
        LocationIconsWithDivider(Modifier.padding(vertical = AppTheme.dimens.margin.tiniest))
        Spacer(modifier = Modifier.width(AppTheme.dimens.margin.small))
        Column(
            Modifier
        ) {
            LocationRow(
                location = trip.start.locality,
                locationPointDate = trip.start.ts.time,
                timeZoneId = trip.tz.id
            )
            Row {
                val distance =
                    stringResource(
                        id = R.string.trip_distance,
                        KiloMeter(trip.distanceMapmatchedKm.toDouble()).toMiles().value
                    )
                val time = trip.durationMillis.milliseconds
                    .inWholeMinutes
                    .toInt()
                    .let { minutes ->
                        pluralStringResource(
                            id = R.plurals.trip_minutes,
                            count = minutes,
                            minutes
                        )
                    }
                Text(
                    text = "$distance, $time",
                    style = AppTheme.typography.text.small,
                    color = AppTheme.colors.text.tableDetails
                )
            }

            LocationRow(
                location = trip.end.locality,
                locationPointDate = trip.end.ts.time,
                timeZoneId = trip.tz.id
            )
        }
    }
}

@Composable
private fun LocationRow(
    location: String,
    locationPointDate: Long,
    timeZoneId: String
) {
    val zoneId = ZoneId.of(timeZoneId)
    val time = Instant.ofEpochMilli(locationPointDate).atZone(zoneId)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = location,
            style = AppTheme.typography.title.small,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(AppTheme.dimens.margin.extraTiny))
        Text(
            text = timeFormatter.format(time),
            style = AppTheme.typography.text.small,
            maxLines = 1
        )
    }
}

@Composable
private fun TripTransportationModeCard(
    transportationMode: UserTransportationMode,
    selectTransportationMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(AppTheme.dimens.margin.tiniest)
            .clip(RoundedCornerShape(TRIP_DETAIL_INFO_CARD_ROUNDED_CORNER))
            .background(AppTheme.colors.background.primary)
            .clickable {
                selectTransportationMode()
            },
        horizontalAlignment = CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraTiny))
        Text(
            text = stringResource(
                id = when (transportationMode) {
                    DRIVER -> R.string.trip_details_driver
                    PASSENGER -> R.string.trip_details_passenger
                    else -> R.string.trip_details_not_a_car
                }
            ),
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.text.small
        )
        Box(modifier = Modifier.padding(horizontal = AppTheme.dimens.margin.tiny)) {
            Image(
                modifier = Modifier
                    .width(TRIP_DETAIL_TRANSPORTATION_MODE_ICON_HEIGHT)
                    .fillMaxHeight(),
                painter = painterResource(
                    id = when (transportationMode) {
                        DRIVER -> R.drawable.ic_driver_trip_mode
                        PASSENGER -> R.drawable.ic_passenger_trip_mode
                        else -> R.drawable.ic_not_a_car_trip_mode
                    }
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun LocationIconsWithDivider(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            modifier = Modifier.height(TRIP_DETAIL_START_END_MARKER_HEIGHT),
            painter = painterResource(
                id = R.drawable.ic_start_marker
            ),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.tiniest))
        Divider(
            color = AppTheme.colors.divider,
            modifier = Modifier
                .width(1.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.tiniest))
        Image(
            modifier = Modifier.height(TRIP_DETAIL_START_END_MARKER_HEIGHT),
            painter = painterResource(id = R.drawable.ic_end_marker),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun DetailInfoHeaderPreview() {
    AppTheme {
        DetailInfoHeader(
            trip = createDummyTrip(),
            selectTransportationMode = {},
            transportationMode = DRIVER
        )
    }
}

@Preview
@Composable
private fun InfoCardPreview() {
    AppTheme {
        Row {
            InfoCard(
                trip = createDummyTrip()
            )
        }
    }
}

@Preview
@Composable
private fun TripTransportationModeCardPreview() {
    AppTheme {
        TripTransportationModeCard(
            transportationMode = DRIVER,
            selectTransportationMode = {}
        )
    }
}

private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

private val TRIP_DETAIL_INFO_CARD_ROUNDED_CORNER = 12.dp
private val TRIP_DETAIL_START_END_MARKER_HEIGHT = 16.dp
private val TRIP_DETAIL_TRANSPORTATION_MODE_ICON_HEIGHT = 52.dp
