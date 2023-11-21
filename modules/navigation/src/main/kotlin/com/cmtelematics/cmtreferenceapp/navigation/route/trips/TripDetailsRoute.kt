package com.cmtelematics.cmtreferenceapp.navigation.route.trips

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class TripDetailsRoute(val tripDriveId: String) : Route(this) {

    override fun toBundle(): Bundle = super.toBundle().apply {
        putString(ARG_TRIP_DRIVE_ID_ID, tripDriveId)
    }

    companion object : Factory<TripDetailsRoute>() {

        private const val ARG_TRIP_DRIVE_ID_ID = "TRIP_DRIVE_ID_ID"

        override val path = "tripDetails"

        override fun create(bundle: Bundle?) = TripDetailsRoute(
            tripDriveId = bundle?.run { getString(ARG_TRIP_DRIVE_ID_ID) }
                ?: error("Trip's driveId was not provided in route!")
        )
    }
}
