package com.cmtelematics.cmtreferenceapp.crash.provider

import android.annotation.SuppressLint
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.Flow

internal interface LocationProvider {

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(): Flow<LocationResult>
}
