package com.cmtelematics.cmtreferenceapp.crash.ui.component

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import com.cmtelematics.cmtreferenceapp.common.ui.util.createMarkerBitmap
import com.cmtelematics.cmtreferenceapp.crash.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
internal fun CrashAssistMap(
    currentLocation: Location,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    CrashAssistMap(
        cameraPositionState = cameraPositionState,
        currentLocation = currentLocation,
        modifier = modifier
    )
}

@Composable
private fun CrashAssistMap(
    currentLocation: Location,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    modifier: Modifier
) {
    val positionIcon = createMarkerBitmap(name = "currentLocation") {
        Image(
            painter = painterResource(id = R.drawable.ic_location_place),
            contentDescription = null
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        googleMapOptionsFactory = {
            GoogleMapOptions().liteMode(false)
        },
        properties = MapProperties(),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false
        ),
        onMapLoaded = {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        currentLocation.latitude,
                        currentLocation.longitude
                    ),
                    LAT_LONG_ZOOM_VALUE
                )
            )
        }
    ) {
        positionIcon?.let { icon ->
            Marker(
                state = MarkerState(
                    position = LatLng(
                        currentLocation.latitude,
                        currentLocation.longitude
                    )
                ),
                icon = BitmapDescriptorFactory.fromBitmap(icon),
                flat = true,
                anchor = Offset(MARKER_ANCHOR_X_OFFSET, MARKER_ANCHOR_Y_OFFSET)
            )
        }
    }
}

private const val MARKER_ANCHOR_X_OFFSET = 0.5f
private const val MARKER_ANCHOR_Y_OFFSET = 0.8f
private const val LAT_LONG_ZOOM_VALUE = 15F
