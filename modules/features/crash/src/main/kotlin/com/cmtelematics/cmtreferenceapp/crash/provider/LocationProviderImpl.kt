package com.cmtelematics.cmtreferenceapp.crash.provider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

internal class LocationProviderImpl @Inject constructor(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Flow<LocationResult> = callbackFlow {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                trySend(locationResult)
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
                    .build(),
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Timber.e(e)
            throw e
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
        .flowOn(dispatcherProvider.main)
        .buffer()
}
