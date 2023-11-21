package com.cmtelematics.cmtreferenceapp.trips.ui

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.cmtelematics.sdk.types.BoundingBox
import com.google.android.gms.maps.model.LatLng

internal fun interface StaticMapRenderer {
    suspend fun renderStaticMap(
        latLngList: List<LatLng>,
        boundingBox: BoundingBox,
        startPositionIcon: Bitmap,
        endPositionIcon: Bitmap
    ): ImageBitmap
}
