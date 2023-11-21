package com.cmtelematics.cmtreferenceapp.common.ui.util

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng

@Composable
fun createMarkerBitmap(
    name: String,
    position: LatLng? = null,
    icon: @Composable () -> Unit
): Bitmap? {
    val bitmapKey = getBitmapKey(name, position)

    var bitmap by remember(bitmapKey) { mutableStateOf<Bitmap?>(null) }

    if (bitmap == null) {
        ComposableAsBitmap(content = icon, onCreated = { bitmap = it })
    }

    return bitmap
}

private fun getBitmapKey(name: String, location: LatLng? = null) =
    location?.let { "Marker(name=$name,location=$it)" } ?: "Marker(name=$name)"
