package com.cmtelematics.cmtreferenceapp.trips.util

import com.cmtelematics.cmtreferenceapp.common.model.KiloMeter
import com.cmtelematics.cmtreferenceapp.trips.model.MapPath
import com.cmtelematics.cmtreferenceapp.trips.model.RouteSegment
import com.cmtelematics.cmtreferenceapp.trips.model.RouteType
import com.cmtelematics.sdk.types.MapPrimaryLineType.ESTIMATED
import com.cmtelematics.sdk.types.MapPrimaryLineType.SPEEDING_HIGH
import com.cmtelematics.sdk.types.MapPrimaryLineType.SPEEDING_LOW
import com.cmtelematics.sdk.types.MapPrimaryLineType.SPEEDING_MEDIUM
import com.cmtelematics.sdk.types.MapScoredTrip
import com.cmtelematics.sdk.types.MapSecondaryLineType.DISTRACTED
import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.MapUnscoredTrip
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

internal fun getRouteSegments(tripDetail: MapTrip, timeZone: TimeZone): List<RouteSegment> =
    when (tripDetail) {
        is MapScoredTrip -> tripDetail.getScoredRouteSegments(timeZone)
        is MapUnscoredTrip -> tripDetail.locations.zipWithNext { startPoint, endPoint ->
            val startLatLng = LatLng(startPoint.latitude, startPoint.longitude)
            val endLatLng = LatLng(endPoint.latitude, endPoint.longitude)
            RouteSegment.Normal(
                start = startLatLng,
                end = endLatLng,
                type = RouteType.Default
            )
        }
        else -> error("Unsupported MapTrip subclass: " + tripDetail::class.java.name)
    }

internal fun List<RouteSegment>.mapToMapPaths(): List<MapPath> = sequence {
    if (isEmpty()) {
        return@sequence
    }

    val currentSection = mutableListOf(first())

    for (segment in this@mapToMapPaths.drop(1)) {
        if (segment.type == currentSection.last().type) {
            currentSection.add(segment)
        } else {
            yield(currentSection.toList())
            currentSection.clear()
            currentSection.add(segment)
        }
    }

    currentSection.takeIf { it.isNotEmpty() }?.let { yield(it.toList()) }
}.map { segments ->
    val line = listOf(segments.first().start) + segments.map { it.end }
    MapPath(path = line, type = segments.first().type)
}.toList()

private fun MapScoredTrip.getScoredRouteSegments(timeZone: TimeZone) =
    waypoints.zipWithNext { startWaypoint, endWaypoint ->
        val startLatLng = LatLng(startWaypoint.lat, startWaypoint.lon)
        val endLatLng = LatLng(endWaypoint.lat, endWaypoint.lon)
        if (startWaypoint.secondaryLineType == DISTRACTED) {
            RouteSegment.Distracted(
                start = startLatLng,
                end = endLatLng,
                type = RouteType.Distracted,
                time = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(startWaypoint.ts.time),
                    ZoneId.of(timeZone.id)
                ),
                speed = KiloMeter(startWaypoint.maxSpeedKmh).toMiles()
            )
        } else {
            RouteSegment.Normal(
                start = startLatLng,
                end = endLatLng,
                type = when (startWaypoint.primaryLineType) {
                    in listOf(SPEEDING_HIGH, SPEEDING_MEDIUM, SPEEDING_LOW) -> RouteType.Speeding
                    ESTIMATED -> RouteType.Estimated
                    else -> RouteType.Default
                }
            )
        }
    }
