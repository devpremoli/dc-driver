package com.cmtelematics.cmtreferenceapp.trips.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.common.model.KiloMeter
import com.cmtelematics.cmtreferenceapp.common.ui.util.fromVectorDrawable
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.EmphasizedText
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.Loaded
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.LoadingFailed
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.LoadingInProgress
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.NotAvailable
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.NotLoaded
import com.cmtelematics.cmtreferenceapp.trips.ui.StaticMapRenderer
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyLoadedTripDetailState
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyTrip
import com.cmtelematics.sdk.types.MapScoredTrip
import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.MapUnscoredTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.TripState
import com.cmtelematics.sdk.types.UserTransportationMode.DRIVER
import com.cmtelematics.sdk.types.UserTransportationMode.PASSENGER
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.format.TextStyle
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun TripItem(
    modifier: Modifier = Modifier,
    trip: ProcessedTripSummary,
    tripDetailState: TripDetailState,
    startPositionIcon: Bitmap?,
    endPositionIcon: Bitmap?,
    staticMapRenderer: StaticMapRenderer
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(TRIP_CARD_ROUNDED_CORNER))
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.background.primary)
        ) {
            TripCardTopDateText(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.margin.default,
                    vertical = AppTheme.dimens.margin.small
                ),
                tripDate = trip.start.ts.time,
                zoneId = ZoneId.of(trip.tz.id)
            )

            TripCardMapContainer(
                trip = trip,
                tripDetailState = tripDetailState,
                startPositionIcon = startPositionIcon,
                endPositionIcon = endPositionIcon,
                staticMapRenderer = staticMapRenderer
            )

            TripCardBottomInfoContainer(
                modifier = Modifier.padding(
                    start = AppTheme.dimens.margin.small,
                    top = AppTheme.dimens.margin.small,
                    bottom = AppTheme.dimens.margin.default
                ),
                trip = trip,
                tripDetailState = tripDetailState
            )
        }

        if (tripDetailState is LoadingInProgress || tripDetailState is NotLoaded) {
            Box(
                modifier = Modifier
                    .background(AppTheme.colors.background.primary)
                    .fillMaxSize()
            ) { CardLoadingIndicator() }
        }
    }
}

@Composable
private fun BoxScope.CardLoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .align(Alignment.Center),
        color = AppTheme.colors.primary
    )
}

@Composable
private fun TripCardTopDateText(modifier: Modifier, tripDate: Long, zoneId: ZoneId) {
    val date = Instant.ofEpochMilli(tripDate).atZone(zoneId)

    val formattedDate = dateFormatter.format(date)
    val formattedTime = timeFormatter.format(date)
    val formattedTimeZone = timeZoneFormatter.format(date)

    EmphasizedText(
        modifier = modifier,
        text = stringResource(R.string.trip_start_date_time, formattedDate, formattedTime, formattedTimeZone),
        textColor = AppTheme.colors.text.tableDetails,
        emphasizedTextStyle = AppTheme.typography.title.medium
    )
}

@Composable
private fun TripCardMapContainer(
    trip: ProcessedTripSummary,
    tripDetailState: TripDetailState,
    startPositionIcon: Bitmap?,
    endPositionIcon: Bitmap?,
    staticMapRenderer: StaticMapRenderer
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(MAP_CONTAINER_ASPECT_RATIO)
    ) {
        if (trip.tripState == TripState.COMPLETE && tripDetailState !is NotAvailable) {
            if (tripDetailState is Loaded) {
                RouteStaticMapImage(
                    tripDetail = tripDetailState.tripDetail,
                    startPositionIcon = startPositionIcon,
                    endPositionIcon = endPositionIcon,
                    staticMapRenderer = staticMapRenderer
                )
            }

            (tripDetailState as? Loaded)?.transportationMode?.let { transportationMode ->
                Image(
                    modifier = Modifier
                        .size(TRIP_TRANSPORTATION_MODE_ICON)
                        .align(alignment = Alignment.TopEnd)
                        .padding(AppTheme.dimens.margin.smaller),
                    contentScale = ContentScale.FillBounds,
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
        } else {
            MapPlaceholderImage()
        }

        if (tripDetailState is LoadingFailed) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = AppTheme.typography.text.large,
                text = stringResource(id = R.string.failed_to_load_trip_detail),
                color = AppTheme.colors.state.danger
            )
        }
    }
}

@Composable
private fun MapPlaceholderImage() {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(
            id = R.drawable.ic_map_placeholder
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
private fun BoxScope.RouteStaticMapImage(
    tripDetail: MapTrip,
    startPositionIcon: Bitmap?,
    endPositionIcon: Bitmap?,
    staticMapRenderer: StaticMapRenderer
) {
    val latLngList = when (tripDetail) {
        is MapScoredTrip -> tripDetail.waypoints?.map { LatLng(it.lat, it.lon) }.orEmpty()
        is MapUnscoredTrip -> tripDetail.locations.map { LatLng(it.latitude, it.longitude) }
        else -> error("Unsupported MapTrip subclass: " + tripDetail::class.java.name)
    }

    var previewBitmap: ImageBitmap? by remember { mutableStateOf(null) }
    LaunchedEffect(startPositionIcon, endPositionIcon) {
        if (!(startPositionIcon == null || endPositionIcon == null)) {
            previewBitmap = staticMapRenderer.renderStaticMap(
                latLngList = latLngList,
                boundingBox = tripDetail.boundingBox,
                startPositionIcon = startPositionIcon,
                endPositionIcon = endPositionIcon
            )
        }
    }

    previewBitmap?.let { loadedPreview ->
        Image(
            bitmap = loadedPreview,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    } ?: CardLoadingIndicator()
}

@Composable
private fun TripCardBottomInfoContainer(
    modifier: Modifier,
    trip: ProcessedTripSummary,
    tripDetailState: TripDetailState
) {
    Column(
        modifier = modifier
    ) {
        val tripState = trip.tripState
        if (tripState == TripState.COMPLETE) {
            TripCardTripInfoText(trip, tripDetailState)
        } else {
            Text(
                text = stringResource(
                    id = when (tripState) {
                        TripState.RECORDING -> R.string.trip_is_recording
                        TripState.WAITING_FOR_UPLOAD,
                        TripState.WAITING_FOR_RESULTS -> R.string.trip_is_processing
                        else -> R.string.trip_an_error_occurred
                    }
                ),
                color = AppTheme.colors.text.tableDetails,
                style = AppTheme.typography.text.large
            )
        }
    }
}

@Composable
private fun TripCardTripInfoText(trip: ProcessedTripSummary, tripDetailState: TripDetailState) {
    val isTripDetailAvailable = tripDetailState !is NotAvailable
    EmphasizedText(
        text = when {
            !isTripDetailAvailable -> stringResource(R.string.trip_start_finish_city_no_phone_present)
            trip.start.locality.isNullOrEmpty() || trip.end.locality.isNullOrEmpty() ->
                stringResource(R.string.trip_start_finish_city_location_not_available)
            else -> stringResource(R.string.trip_start_finish_city, trip.start.locality, trip.end.locality)
        },
        style = AppTheme.typography.text.large,
        textColor = AppTheme.colors.text.tableDetails,
        emphasizedTextStyle = AppTheme.typography.title.medium
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraTiny))

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

    val bottomInfoText = if (isTripDetailAvailable) {
        val distance = stringResource(
            id = R.string.trip_distance,
            KiloMeter(trip.distanceMapmatchedKm.toDouble()).toMiles().value
        )
        val eventCount = ((tripDetailState as? Loaded)?.tripDetail as? MapScoredTrip)?.events?.size ?: 0
        val events = pluralStringResource(
            id = R.plurals.trip_events,
            count = eventCount,
            eventCount
        )

        "$distance, $time | $events"
    } else {
        time
    }

    Text(
        text = bottomInfoText,
        color = AppTheme.colors.text.tableDetails
    )
}

@Preview
@Composable
private fun TripItemPreview() {
    val placeholder = fromVectorDrawable(id = R.drawable.ic_map_placeholder)
    AppTheme {
        TripItem(
            trip = createDummyTrip(),
            tripDetailState = createDummyLoadedTripDetailState(),
            startPositionIcon = null,
            endPositionIcon = null,
            staticMapRenderer = { _, _, _, _ -> placeholder }
        )
    }
}

@Preview
@Composable
private fun TripItemLoadingPreview() {
    val placeholder = fromVectorDrawable(id = R.drawable.ic_map_placeholder)
    AppTheme {
        TripItem(
            trip = createDummyTrip(),
            tripDetailState = LoadingInProgress,
            startPositionIcon = null,
            endPositionIcon = null,
            staticMapRenderer = { _, _, _, _ -> placeholder }
        )
    }
}

@Preview
@Composable
private fun TripItemLoadingFailedPreview() {
    val placeholder = fromVectorDrawable(id = R.drawable.ic_map_placeholder)
    AppTheme {
        TripItem(
            trip = createDummyTrip(),
            tripDetailState = LoadingFailed,
            startPositionIcon = null,
            endPositionIcon = null,
            staticMapRenderer = { _, _, _, _ -> placeholder }
        )
    }
}

@Preview
@Composable
private fun ProcessingTripItemPreview() {
    val placeholder = fromVectorDrawable(id = R.drawable.ic_map_placeholder)
    AppTheme {
        TripItem(
            trip = createDummyTrip().also {
                it.tripState = TripState.WAITING_FOR_UPLOAD
            },
            tripDetailState = createDummyLoadedTripDetailState(),
            startPositionIcon = null,
            endPositionIcon = null,
            staticMapRenderer = { _, _, _, _ -> placeholder }
        )
    }
}

@Preview
@Composable
private fun TagOnlyTripItemPreview() {
    val placeholder = fromVectorDrawable(id = R.drawable.ic_map_placeholder)
    AppTheme {
        TripItem(
            trip = createDummyTrip().also {
                it.tagOnly = true
            },
            tripDetailState = NotAvailable,
            startPositionIcon = null,
            endPositionIcon = null,
            staticMapRenderer = { _, _, _, _ -> placeholder }
        )
    }
}

private val TRIP_CARD_ROUNDED_CORNER = 12.dp
private val TRIP_TRANSPORTATION_MODE_ICON = 76.dp

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
private val timeZoneFormatter = DateTimeFormatterBuilder().appendLocalizedOffset(TextStyle.SHORT).toFormatter()

const val MAP_CONTAINER_ASPECT_RATIO = 2.5f
