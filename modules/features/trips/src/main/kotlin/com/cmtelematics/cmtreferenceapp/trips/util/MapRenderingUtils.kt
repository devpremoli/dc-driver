package com.cmtelematics.cmtreferenceapp.trips.util

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.DEFAULT_CAMERA_POSITION_PADDING
import com.cmtelematics.cmtreferenceapp.trips.ui.component.marker.MARKER_ANCHOR_X_OFFSET
import com.cmtelematics.cmtreferenceapp.trips.ui.component.marker.MARKER_ANCHOR_Y_OFFSET
import com.cmtelematics.sdk.types.BoundingBox
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import com.google.maps.android.ktx.awaitMapLoad
import com.google.maps.android.ktx.awaitSnapshot

internal suspend fun renderMap(
    map: GoogleMap,
    polylineOptions: PolylineOptions,
    bounds: BoundingBox,
    startPositionIcon: Bitmap,
    endPositionIcon: Bitmap
): ImageBitmap {
    map.addPolyline {
        color(polylineOptions.polylineColor.toArgb())
        width(polylineOptions.polylineWidth)
        addAll(polylineOptions.path)
    }

    map.addMarker {
        position(polylineOptions.path.first())
        anchor(MARKER_ANCHOR_X_OFFSET, MARKER_ANCHOR_Y_OFFSET)
        icon(BitmapDescriptorFactory.fromBitmap(startPositionIcon))
        flat(true)
    }

    map.addMarker {
        position(polylineOptions.path.last())
        anchor(MARKER_ANCHOR_X_OFFSET, MARKER_ANCHOR_Y_OFFSET)
        icon(BitmapDescriptorFactory.fromBitmap(endPositionIcon))
        flat(true)
    }

    val northEastLatLng = LatLng(bounds.maxLat.toDouble(), bounds.maxLon.toDouble())
    val southWestLatLng = LatLng(bounds.minLat.toDouble(), bounds.minLon.toDouble())

    map.moveCamera(
        CameraUpdateFactory.newLatLngBounds(
            LatLngBounds(southWestLatLng, northEastLatLng),
            DEFAULT_CAMERA_POSITION_PADDING
        )
    )
    map.awaitMapLoad()
    val imageBitmap = map.awaitSnapshot()?.asImageBitmap() ?: error("Failed to render snapshot.")
    map.clear()

    return imageBitmap
}

internal data class PolylineOptions(
    val polylineColor: Color,
    val polylineWidth: Float,
    val path: List<LatLng>
)
