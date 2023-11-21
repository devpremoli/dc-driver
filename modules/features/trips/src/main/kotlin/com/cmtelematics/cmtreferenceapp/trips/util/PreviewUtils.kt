package com.cmtelematics.cmtreferenceapp.trips.util

import com.cmtelematics.cmtreferenceapp.common.ui.util.makeDummyVehicle
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState
import com.cmtelematics.sdk.types.BoundingBox
import com.cmtelematics.sdk.types.DateTimePosition
import com.cmtelematics.sdk.types.DriveStartStopMethod
import com.cmtelematics.sdk.types.LatLng
import com.cmtelematics.sdk.types.MapEvent
import com.cmtelematics.sdk.types.MapScoredTrip
import com.cmtelematics.sdk.types.MapWaypoint
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.TripState
import com.cmtelematics.sdk.types.UserTransportationMode
import java.util.Date
import java.util.TimeZone

internal fun createDummyTripList(): List<Pair<ProcessedTripSummary, TripDetailState>> = listOf(
    createDummyTrip() to createDummyLoadedTripDetailState(),
    createDummyTrip().also {
        it.tripState = TripState.WAITING_FOR_UPLOAD
    } to createDummyLoadedTripDetailState()
)

internal fun createDummyTrip(): ProcessedTripSummary = ProcessedTripSummary(
    "", // driveId
    0L, // processingSequence
    false, // hide
    TimeZone.getDefault(), // timezone
    DateTimePosition(Date(), 0f, 0f, "Start", "", "US"), // start
    DateTimePosition(Date(), 0f, 0f, "End", "", "US"), // end
    0f, // distanceMapmatchedKm
    TripState.COMPLETE, // tripState
    0f, // starRating
    0f, // starRatingAccel
    0f, // starRatingBrake
    0f, // starRatingTurn
    0f, // starRatingSpeeding
    0f, // starRatingPhoneMotion
    0f, // starRatingNight
    0f, // starRatingSmoothness
    0f, // starRatingAwareness
    0f, // starRatingRoads
    false, // night
    "", // tagMacAddress
    false, // primaryDriver
    0f, // passengerStarRating
    false, // tagOnly
    UserTransportationMode.BIKE, // classificationLabel
    0f, // score
    0f, // passengerScore
    DriveStartStopMethod.AUTOMATIC,
    makeDummyVehicle("vehicle1") // vehicle
)

internal fun createDummyMapScoredTrip(): MapScoredTrip = MapScoredTrip(
    BoundingBox(0f, 0f, 0f, 0f),
    emptyList<MapEvent>(),
    listOf(
        MapWaypoint(
            0.0,
            0.0,
            Date(),
            0.0,
            0.0,
            0.0,
            0,
            IntArray(0),
            false
        ),
        MapWaypoint(
            0.0,
            0.0,
            Date(),
            0.0,
            0.0,
            0.0,
            0,
            IntArray(0),
            false
        )
    ),
    emptyList<LatLng>()
)

internal fun createDummyLoadedTripDetailState(): TripDetailState = TripDetailState.Loaded(
    tripDetail = createDummyMapScoredTrip(),
    transportationMode = UserTransportationMode.DRIVER
)
