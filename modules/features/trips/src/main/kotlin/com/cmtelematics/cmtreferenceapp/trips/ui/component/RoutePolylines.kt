package com.cmtelematics.cmtreferenceapp.trips.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.trips.model.MapPath
import com.cmtelematics.cmtreferenceapp.trips.model.RouteType.Default
import com.cmtelematics.cmtreferenceapp.trips.model.RouteType.Distracted
import com.cmtelematics.cmtreferenceapp.trips.model.RouteType.Estimated
import com.cmtelematics.cmtreferenceapp.trips.model.RouteType.Speeding
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.POLYLINE_WIDTH
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RoutePolylines(mapPaths: ImmutableList<MapPath>) {
    val polylineWidth = with(LocalDensity.current) {
        POLYLINE_WIDTH.toPx()
    }

    mapPaths.forEach { (path, type) ->
        if (type != Distracted) {
            Polyline(
                points = path,
                color = when (type) {
                    Default -> AppTheme.colors.lineSegment.default
                    Speeding -> AppTheme.colors.lineSegment.speeding
                    Estimated -> AppTheme.colors.lineSegment.estimated
                    else -> error("Unsupported RouteSegment type: ${type.name}")
                },
                width = polylineWidth
            )
        } else {
            DistractedPolyline(points = path, polylineWidth)
        }
    }
}

@Composable
private fun DistractedPolyline(points: List<LatLng>, defaultPolylineWidth: Float) {
    val outerPolylineWidth = with(LocalDensity.current) {
        DISTRACTION_OUTER_POLYLINE_WIDTH.toPx()
    }

    Polyline(
        points = points,
        color = AppTheme.colors.lineSegment.distraction,
        width = outerPolylineWidth
    )

    Polyline(
        points = points,
        color = AppTheme.colors.lineSegment.default,
        width = defaultPolylineWidth
    )
}

private val DISTRACTION_OUTER_POLYLINE_WIDTH = 8.dp
