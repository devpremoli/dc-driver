package com.cmtelematics.cmtreferenceapp.navigation.route.tags

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateVehicleRoute : Route(this) {

    companion object : Factory<CreateVehicleRoute>() {

        override val path = "createVehicle"

        override fun create(bundle: Bundle?) = CreateVehicleRoute()
    }
}
