package com.cmtelematics.cmtreferenceapp.trips.util

import com.cmtelematics.cmtreferenceapp.common.model.KiloMeter
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Acceleration
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Braking
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Cornering
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Distraction
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent.Speeding
import com.cmtelematics.sdk.types.MapEventType
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.seconds
import com.cmtelematics.sdk.types.MapEvent as SdkMapEvent

internal fun getMapEvent(mapEvent: SdkMapEvent, timeZoneId: String) = when (mapEvent.eventType) {
    MapEventType.HARD_BRAKE -> Braking(
        eventPoint = mapEvent.getEventPoint(),
        time = mapEvent.getTime(timeZoneId)
    )
    MapEventType.HARD_ACCEL -> Acceleration(
        eventPoint = mapEvent.getEventPoint(),
        time = mapEvent.getTime(timeZoneId)
    )
    MapEventType.HARD_TURN -> Cornering(
        eventPoint = mapEvent.getEventPoint(),
        time = mapEvent.getTime(timeZoneId)
    )
    MapEventType.SPEEDING -> Speeding(
        eventPoint = mapEvent.getEventPoint(),
        time = mapEvent.getTime(timeZoneId),
        speedLimit = KiloMeter(mapEvent.speedLimitKmh).toMiles(),
        actualSpeed = KiloMeter(mapEvent.speedKmh).toMiles()
    )
    MapEventType.PHONE_MOVED,
    MapEventType.CALLING,
    MapEventType.TAPPING -> Distraction(
        eventPoint = mapEvent.getEventPoint(),
        time = mapEvent.getTime(timeZoneId),
        length = mapEvent.durationSec.toLong().seconds
    )
    else -> error("Unsupported eventType: ${mapEvent.eventType}")
}

private fun SdkMapEvent.getEventPoint() = LatLng(lat, lon)
private fun SdkMapEvent.getTime(timeZoneId: String) = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(ts.time),
    ZoneId.of(timeZoneId)
)
