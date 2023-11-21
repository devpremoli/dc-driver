package com.cmtelematics.cmtreferenceapp.wrappers.crash.util

import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashLocation
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.ACCURACY
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.ALTITUDE
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.DEVICE_ID
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.HEADING
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.INTENSITY
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.LATITUDE
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.LONGITUDE
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.FakeCrash.USER_ID
import java.time.Clock
import kotlin.random.Random

/**
 * A fake crash object used for crash simulation.
 */
internal fun generateFakeCrash(clock: Clock): Crash {
    val instant = clock.instant()
    val crashLocation = CrashLocation(
        dateTime = instant,
        latitude = LATITUDE,
        longitude = LONGITUDE,
        altitude = ALTITUDE,
        accuracy = ACCURACY,
        heading = HEADING
    )
    return Crash(
        id = Random.nextLong(),
        userId = USER_ID,
        intensity = INTENSITY,
        timestamp = instant,
        location = crashLocation,
        verified = true,
        verifiedTimestamp = instant,
        deviceId = DEVICE_ID
    )
}

private object FakeCrash {
    const val USER_ID = 41901507L
    const val INTENSITY = 1
    const val DEVICE_ID = "949D9290-C1EA-4963-A9DF-E6D1C1E35C95"
    const val LATITUDE = 42.36265f
    const val LONGITUDE = -71.088f
    const val ALTITUDE = 56.56f
    const val ACCURACY = 90.5f
    const val HEADING = 100.0f
}
