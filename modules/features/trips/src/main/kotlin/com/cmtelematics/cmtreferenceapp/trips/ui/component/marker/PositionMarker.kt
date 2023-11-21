package com.cmtelematics.cmtreferenceapp.trips.ui.component.marker

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
internal fun PositionMarker(
    position: LatLng,
    icon: Bitmap
) {
    Marker(
        state = MarkerState(position = position),
        icon = BitmapDescriptorFactory.fromBitmap(icon),
        flat = true,
        anchor = Offset(MARKER_ANCHOR_X_OFFSET, MARKER_ANCHOR_Y_OFFSET)
    )
}

const val MARKER_ANCHOR_X_OFFSET = 0.5f
const val MARKER_ANCHOR_Y_OFFSET = 0.8f
