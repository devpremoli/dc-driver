package com.cmtelematics.cmtreferenceapp.trips.model

import com.google.android.gms.maps.model.LatLng

internal data class MapPath(
    val path: List<LatLng>,
    val type: RouteType
)
