package com.cmtelematics.cmtreferenceapp.navigation.route.crash

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class AmbulanceRequestedRoute : Route(this) {

    companion object : Factory<AmbulanceRequestedRoute>() {
        override val path = "ambulanceRequestedRoute"
        override fun create(bundle: Bundle?) = AmbulanceRequestedRoute()
    }
}
