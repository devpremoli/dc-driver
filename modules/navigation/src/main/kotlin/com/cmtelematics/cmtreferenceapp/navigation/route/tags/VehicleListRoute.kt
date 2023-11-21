package com.cmtelematics.cmtreferenceapp.navigation.route.tags

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class VehicleListRoute : Route(this) {

    companion object : Factory<VehicleListRoute>() {

        override val path = "vehicleList"

        override fun create(bundle: Bundle?) = VehicleListRoute()
    }
}
