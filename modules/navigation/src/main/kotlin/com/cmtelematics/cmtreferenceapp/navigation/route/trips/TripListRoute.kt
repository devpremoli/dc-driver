package com.cmtelematics.cmtreferenceapp.navigation.route.trips

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class TripListRoute : Route(this) {

    companion object : Factory<TripListRoute>() {

        override val path = "tripList"

        override fun create(bundle: Bundle?) = TripListRoute()
    }
}
