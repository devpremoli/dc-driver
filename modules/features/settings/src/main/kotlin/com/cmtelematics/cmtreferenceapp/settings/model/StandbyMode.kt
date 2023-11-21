package com.cmtelematics.cmtreferenceapp.settings.model

import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal enum class StandbyMode(val standByTimeInMinutes: Int) {
    OFF(0),
    ONE_MINUTE(1),
    ONE_HOUR(1.hoursInMinutes()),
    SIX_HOURS(6.hoursInMinutes()),
    ONE_DAY(1.daysInMinutes()),
    THREE_DAYS(3.daysInMinutes()),
    SEVEN_DAYS(7.daysInMinutes())
}

private fun Int.hoursInMinutes() = hours.inWholeMinutes.toInt()
private fun Int.daysInMinutes() = days.inWholeMinutes.toInt()
