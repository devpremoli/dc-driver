package com.cmtelematics.cmtreferenceapp.wrappers.trip

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperBatteryIsLowForTripRecordingHolder
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager.TripManagerException
import com.cmtelematics.sdk.MapDataReader
import com.cmtelematics.sdk.types.Delay
import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.QueuedNetworkCallback
import com.cmtelematics.sdk.types.TripList
import com.cmtelematics.sdk.types.TripListChange
import com.cmtelematics.sdk.types.TripListResponse
import com.cmtelematics.sdk.types.TripState
import com.cmtelematics.sdk.types.UserTransportationMode
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.cmtelematics.sdk.TripManager as SdkTripManager

internal class TripManagerImpl @Inject constructor(
    private val sdkTripManager: SdkTripManager,
    private val mapDataReader: MapDataReader,
    bus: Bus,
    private val dispatcherProvider: DispatcherProvider,
    wrapperBatteryIsLowForTripRecordingHolder: WrapperBatteryIsLowForTripRecordingHolder
) : TripManager {
    override val tripList = MutableStateFlow<List<ProcessedTripSummary>>(emptyList())

    override val batteryIsLowForTripRecording: StateFlow<Boolean> =
        wrapperBatteryIsLowForTripRecordingHolder.batteryIsLowForTripRecording.asStateFlow()

    init {
        bus.register(this)

        sdkTripManager.loadTripList()
    }

    override suspend fun refreshTripList() = withContext(dispatcherProvider.main) {
        suspendCoroutine { continuation ->
            val listener = createTripListUpdateListener(continuation)
            sdkTripManager.pullTripList(Delay.FORCED, listener)
        }
    }

    override suspend fun getTripDetails(tripDriveId: String): MapTrip? {
        val tripState: TripState = tripList.value.first { it.driveId == tripDriveId }.tripState

        return if (tripState == TripState.COMPLETE) {
            mapDataReader.getScoredDrive(tripDriveId)
        } else {
            mapDataReader.getPendingDrive(tripDriveId)
        }
    }

    @Subscribe
    fun handleTripsChanged(newTripList: TripList) {
        Timber.i("Trip list is changed. One of a trip's state is changed or a new trip is available")
        tripList.value = newTripList.trips
    }

    @Subscribe
    fun handleTripListChanges(tripListChange: TripListChange) {
        Timber.i("There is a change in trip list via: $tripListChange")
    }

    private fun createTripListUpdateListener(
        continuation: Continuation<Unit>
    ) = object : QueuedNetworkCallback<TripListResponse> {

        override fun post(response: TripListResponse) {
            if (response.httpCode in successfulHttpCode) {
                Timber.i("Update trip list process successfully finished: " + response.drives)
                continuation.resume(Unit)
            } else {
                val errorMessage = "Failed to update trip list" + if (response.httpCode > 0) {
                    ", HTTP code: ${response.httpCode}"
                } else {
                    ""
                }
                Timber.e(errorMessage)
                continuation.resumeWithException(
                    TripManagerException(errorMessage)
                )
            }
        }

        override fun deauthorized(response: TripListResponse) {
            // post function will also be called if user is not authorized
        }

        override fun enqueued() {
            Timber.i("Update trip list request enqueued.")
        }

        override fun skipped() {
            Timber.e("Update trip list request skipped.")
            continuation.resumeWithException(TripManagerException("Update trip list request skipped."))
        }
    }

    override suspend fun setUserTransportationMode(
        tripDriveId: String,
        transportationMode: UserTransportationMode
    ) = withContext(dispatcherProvider.main) {
        Timber.i("TransportationMode is changing to: $transportationMode in $tripDriveId")
        sdkTripManager.setUserTransportationMode(tripDriveId, transportationMode)
    }

    override suspend fun getUserTransportationMode(
        trip: ProcessedTripSummary
    ): UserTransportationMode? = withContext(dispatcherProvider.io) {
        sdkTripManager.getEffectiveLabel(trip)
    }

    companion object {
        private val successfulHttpCode = 200 until 300
    }
}
