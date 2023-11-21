package com.cmtelematics.cmtreferenceapp.navigation.route.tags

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.parcelize.Parcelize

@Parcelize
class LinkTagRoute(val vehicleShortId: Long) : Route(this) {

    override fun toBundle(): Bundle = super.toBundle().apply {
        putLong(ARG_VEHICLE_SHORT_ID, vehicleShortId)
    }

    companion object : Factory<LinkTagRoute>() {

        private const val ARG_VEHICLE_SHORT_ID = "VEHICLE_SHORT_ID"

        override val path = "linkTag"

        override fun create(bundle: Bundle?) = LinkTagRoute(
            vehicleShortId = bundle?.run { getLong(ARG_VEHICLE_SHORT_ID).takeIf { it != 0L } }
                ?: error("Vehicle ID was not provided in route!")
        )
    }
}
