package com.cmtelematics.cmtreferenceapp.wrappers.trip

import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.UserTransportationMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Provides services related to Trip management.
 */
interface TripManager {
    /**
     * The currently cached list of trips. Call [refreshTripList] to update from the server.
     */
    val tripList: Flow<List<ProcessedTripSummary>>

    /**
     * The current state of battery based trip detection ability of DriveWell SDK.
     * If battery level is low the SDK is unable to detect trips.
     */
    val batteryIsLowForTripRecording: StateFlow<Boolean>

    /**
     * Updates the list of cached trips in [tripList]. This functions waits for the operation to complete and
     * will return once it is concluded.
     *
     * Any network errors encountered will be rethrown.
     */
    suspend fun refreshTripList()

    /**
     * Gets the trip detail [MapTrip] object of a trip.
     *
     * @param tripDriveId the driveId of the trip
     * @return the detail object of the trip
     *
     * Any network errors encountered will be rethrown.
     */
    suspend fun getTripDetails(tripDriveId: String): MapTrip?

    /**
     * Sets the transportation mode [UserTransportationMode] of the trip.
     *
     * @param tripDriveId the driveId of the trip
     * @param transportationMode the selected transportation mode of the trip
     */
    suspend fun setUserTransportationMode(tripDriveId: String, transportationMode: UserTransportationMode)

    /**
     * Gets the transportation mode [UserTransportationMode] object of a trip.
     *
     * @param trip the trip of object of the desired trip
     * @return the transportation mode of the trip
     */
    suspend fun getUserTransportationMode(trip: ProcessedTripSummary): UserTransportationMode?

    /**
     * Thrown by [refreshTripList] if there's an issue encountered while updating trip list.
     *
     * @param message a user readable exception message
     */
    class TripManagerException(message: String) : Exception(message)
}
