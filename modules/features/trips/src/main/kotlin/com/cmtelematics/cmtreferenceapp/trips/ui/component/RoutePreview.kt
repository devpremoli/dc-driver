package com.cmtelematics.cmtreferenceapp.trips.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.cmtelematics.cmtreferenceapp.common.ui.util.createMarkerBitmap
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Acceleration
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Braking
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Cornering
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Distraction
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Speeding
import com.cmtelematics.cmtreferenceapp.trips.model.MapPath
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.DEFAULT_CAMERA_POSITION_PADDING
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.MARKER_NAME_END
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.MARKER_NAME_START
import com.cmtelematics.cmtreferenceapp.trips.ui.component.marker.EventMarker
import com.cmtelematics.cmtreferenceapp.trips.ui.component.marker.PositionMarker
import com.cmtelematics.sdk.types.BoundingBox
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RoutePreview(
    modifier: Modifier = Modifier,
    mapPaths: ImmutableList<MapPath>,
    events: ImmutableList<MapEvent>,
    mapBoundingBox: BoundingBox,
    showEventInfo: (MapEvent) -> Unit,
    contentPaddingTop: Dp
) {
    val startPosition = mapPaths.firstOrNull()?.run { path.first() }
    val endPosition = mapPaths.lastOrNull()?.run { path.last() }

    val northEastLatLng = remember(mapBoundingBox) {
        LatLng(mapBoundingBox.maxLat.toDouble(), mapBoundingBox.maxLon.toDouble())
    }

    val southWestLatLng = remember(mapBoundingBox) {
        LatLng(mapBoundingBox.minLat.toDouble(), mapBoundingBox.minLon.toDouble())
    }

    val mapEdgesLatLngBounds = remember(northEastLatLng, southWestLatLng) {
        getMapEdgesLatLngBounds(northEastLatLng, southWestLatLng)
    }

    val cameraPositionState = rememberCameraPositionState()

    var needToRestoreDefaultLocation by remember { mutableStateOf(false) }
    var isMapLoaded by remember { mutableStateOf(false) }

    Box {
        RoutePreview(
            mapPaths = mapPaths,
            events = events,
            startPosition = startPosition,
            endPosition = endPosition,
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            mapEdgesLatLngBounds = mapEdgesLatLngBounds,
            showEventInfo = showEventInfo,
            contentPaddingTop = contentPaddingTop,
            needToRestoreDefaultLocation = needToRestoreDefaultLocation,
            locationRestoredToDefault = { needToRestoreDefaultLocation = false },
            onMapLoaded = { isMapLoaded = true }
        )

        if (isMapLoaded) {
            RestoreDefaultLocationButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(AppTheme.dimens.margin.tiny)
            ) {
                needToRestoreDefaultLocation = true
            }
        }
    }
}

@Composable
private fun RoutePreview(
    mapPaths: ImmutableList<MapPath>,
    events: ImmutableList<MapEvent>,
    startPosition: LatLng?,
    endPosition: LatLng?,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    properties: MapProperties = MapProperties(),
    mapEdgesLatLngBounds: LatLngBounds,
    showEventInfo: (MapEvent) -> Unit,
    contentPaddingTop: Dp,
    needToRestoreDefaultLocation: Boolean,
    locationRestoredToDefault: () -> Unit,
    onMapLoaded: () -> Unit
) {
    val startPositionIcon = createMarkerBitmapForPosition(
        markerPosition = startPosition,
        bitmapName = MARKER_NAME_START,
        markerIconResource = R.drawable.ic_start_marker
    )

    val endPositionIcon = createMarkerBitmapForPosition(
        markerPosition = endPosition,
        bitmapName = MARKER_NAME_END,
        markerIconResource = R.drawable.ic_end_marker
    )

    val eventIcons = mapOf(
        MARKER_NAME_DISTRACTION to R.drawable.ic_distraction_marker,
        MARKER_NAME_SPEEDING to R.drawable.ic_speeding_marker,
        MARKER_NAME_ACCELERATION to R.drawable.ic_acceleration_marker,
        MARKER_NAME_BRAKING to R.drawable.ic_braking_marker,
        MARKER_NAME_CORNERING to R.drawable.ic_cornering_marker
    ).map { (name, resource) ->
        name to createMarkerBitmap(name = name) {
            Image(
                painter = painterResource(id = resource),
                contentDescription = null
            )
        }
    }

    LaunchedEffect(needToRestoreDefaultLocation) {
        if (needToRestoreDefaultLocation) {
            moveCameraToDefaultLocation(cameraPositionState, mapEdgesLatLngBounds)
            locationRestoredToDefault()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        googleMapOptionsFactory = { GoogleMapOptions().liteMode(false) },
        properties = properties,
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false
        ),
        onMapLoaded = {
            moveCameraToDefaultLocation(cameraPositionState, mapEdgesLatLngBounds)
            onMapLoaded()
        },
        contentPadding = PaddingValues(top = contentPaddingTop)
    ) {
        startPositionIcon?.let { PositionMarker(startPosition ?: error("startPosition must be not null"), it) }

        endPositionIcon?.let { PositionMarker(endPosition ?: error("endPosition must be not null"), it) }

        events.forEach { mapEvent ->
            val mapEventBitmapName = mapEvent.getMapEventBitmapName()
            val (_, icon) = eventIcons.first { (bitmapNameKey, _) -> bitmapNameKey == mapEventBitmapName }
            icon?.let { EventMarker(it, mapEvent, showEventInfo) }
        }

        RoutePolylines(mapPaths)
    }
}

@Composable
private fun createMarkerBitmapForPosition(
    markerPosition: LatLng?,
    bitmapName: String,
    @DrawableRes markerIconResource: Int
) = markerPosition?.let { position ->
    createMarkerBitmap(name = bitmapName, position = position) {
        Image(
            painter = painterResource(id = markerIconResource),
            contentDescription = null
        )
    }
}

private fun MapEvent.getMapEventBitmapName() = when (this) {
    is Acceleration -> MARKER_NAME_ACCELERATION
    is Braking -> MARKER_NAME_ACCELERATION
    is Cornering -> MARKER_NAME_ACCELERATION
    is Distraction -> MARKER_NAME_ACCELERATION
    is Speeding -> MARKER_NAME_ACCELERATION
}

private fun moveCameraToDefaultLocation(cameraPositionState: CameraPositionState, mapEdgesLatLngBounds: LatLngBounds) {
    cameraPositionState.move(
        CameraUpdateFactory.newLatLngBounds(
            mapEdgesLatLngBounds,
            DEFAULT_CAMERA_POSITION_PADDING
        )
    )
}

private fun getMapEdgesLatLngBounds(startPosition: LatLng, endPosition: LatLng) = LatLngBounds.Builder()
    .include(startPosition)
    .include(endPosition)
    .build()

private const val MARKER_NAME_DISTRACTION = "distraction"
private const val MARKER_NAME_SPEEDING = "speeding"
private const val MARKER_NAME_CORNERING = "cornering"
private const val MARKER_NAME_ACCELERATION = "acceleration"
private const val MARKER_NAME_BRAKING = "acceleration"
