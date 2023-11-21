package com.cmtelematics.cmtreferenceapp.common.model

@JvmInline
value class KiloMeter(val value: Double) {
    fun toMiles() = Mile(value * MILE_MULTIPLIER)
}

@JvmInline
value class Mile(val value: Double) {
    fun toKiloMeters() = KiloMeter(value / MILE_MULTIPLIER)
}

private const val MILE_MULTIPLIER = 0.621371
