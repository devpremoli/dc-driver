package com.cmtelematics.cmtreferenceapp.trips.model

import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.UserTransportationMode

/**
 * Represents the current state of trip detail.
 */
internal sealed interface TripDetailState {
    object NotLoaded : TripDetailState
    object LoadingInProgress : TripDetailState
    data class Loaded(val tripDetail: MapTrip, val transportationMode: UserTransportationMode?) : TripDetailState
    object LoadingFailed : TripDetailState
    object NotAvailable : TripDetailState
}
