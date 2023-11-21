package com.cmtelematics.cmtreferenceapp.trips.model

import com.cmtelematics.cmtreferenceapp.common.model.Mile
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import kotlin.time.Duration

internal sealed interface MapEvent {
    val eventPoint: LatLng
    val time: LocalDateTime

    data class Distraction(
        override val eventPoint: LatLng,
        override val time: LocalDateTime,
        val length: Duration
    ) : MapEvent

    data class Cornering(
        override val eventPoint: LatLng,
        override val time: LocalDateTime
    ) : MapEvent

    data class Speeding(
        override val eventPoint: LatLng,
        override val time: LocalDateTime,
        val speedLimit: Mile,
        val actualSpeed: Mile
    ) : MapEvent

    data class Braking(
        override val eventPoint: LatLng,
        override val time: LocalDateTime
    ) : MapEvent

    data class Acceleration(
        override val eventPoint: LatLng,
        override val time: LocalDateTime
    ) : MapEvent
}
