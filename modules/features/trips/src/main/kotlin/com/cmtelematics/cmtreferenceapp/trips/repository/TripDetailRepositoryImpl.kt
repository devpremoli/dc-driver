package com.cmtelematics.cmtreferenceapp.trips.repository

import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import com.cmtelematics.sdk.types.ProcessedTripSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TripDetailRepositoryImpl @Inject constructor(
    private val tripManager: TripManager
) : TripDetailRepository {

    private val mutex = Mutex()

    private val mutableDetailCache: MutableStateFlow<Map<String, TripDetailState>> = MutableStateFlow(emptyMap())

    override val detailCache: StateFlow<Map<String, TripDetailState>> = mutableDetailCache.asStateFlow()

    override suspend fun loadTripDetails(trip: ProcessedTripSummary) = mutex.withLock {
        val tripDriveId = trip.driveId
        mutableDetailCache.value = detailCache.value + (tripDriveId to TripDetailState.LoadingInProgress)
        try {
            val tripDetail = tripManager.getTripDetails(tripDriveId)
            val transportationMode = tripManager.getUserTransportationMode(trip)
            mutableDetailCache.value = detailCache.value + (
                tripDriveId to (
                    tripDetail?.let { mapTrip ->
                        TripDetailState.Loaded(
                            tripDetail = mapTrip,
                            transportationMode = transportationMode ?: trip.classificationLabel
                        )
                    } ?: TripDetailState.NotAvailable
                    )
                )
        } catch (exception: Exception) {
            mutableDetailCache.value = detailCache.value + (tripDriveId to TripDetailState.LoadingFailed)
            throw exception
        }
    }

    override suspend fun evictDetail(driveId: String) = mutex.withLock {
        mutableDetailCache.value = mutableDetailCache.value - driveId
    }

    override suspend fun clear() = mutex.withLock {
        mutableDetailCache.value = emptyMap()
    }
}
