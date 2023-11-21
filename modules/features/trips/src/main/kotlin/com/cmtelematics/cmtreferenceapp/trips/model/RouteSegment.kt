package com.cmtelematics.cmtreferenceapp.trips.model

import com.cmtelematics.cmtreferenceapp.common.model.Mile
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

internal sealed interface RouteSegment {
    val start: LatLng
    val end: LatLng
    val type: RouteType

    data class Normal(
        override val start: LatLng,
        override val end: LatLng,
        override val type: RouteType
    ) : RouteSegment

    data class Distracted(
        override val start: LatLng,
        override val end: LatLng,
        override val type: RouteType,
        val time: LocalDateTime,
        val speed: Mile
    ) : RouteSegment
}
