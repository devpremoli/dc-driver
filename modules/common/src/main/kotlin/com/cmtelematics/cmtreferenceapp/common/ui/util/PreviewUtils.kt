package com.cmtelematics.cmtreferenceapp.common.ui.util

import com.cmtelematics.sdk.types.Vehicle
import com.cmtelematics.sdk.types.VehicleType

fun makeDummyVehicle(
    nickname: String,
    make: String? = null,
    model: String? = null,
    tagLinked: Boolean = false
) = Vehicle(
    0,
    null,
    null,
    null,
    make,
    model,
    nickname,
    0f,
    VehicleType.CAR,
    false,
    0,
    0,
    null,
    null,
    null,
    null,
    tagLinked,
    null,
    null,
    null
)
