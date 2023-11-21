package com.cmtelematics.cmtreferenceapp.wrappers.crash.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Information of crash.
 */
@Serializable
data class Crash(
    @SerialName("id") val id: Long = 0,
    @SerialName("user_id") val userId: Long = 0,
    @SerialName("intensity") val intensity: Int = 0,
    @SerialName("crash_datetime")
    @Serializable(with = InstantSerializer::class) val timestamp: Instant? = null,
    @SerialName("crash_location") val location: CrashLocation? = null,
    @SerialName("cmt_verified") val verified: Boolean? = null,
    @SerialName("cmt_verified_datetime")
    @Serializable(with = InstantSerializer::class) val verifiedTimestamp: Instant? = null,
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("responded") val responded: Boolean = false
) : Comparable<Crash> {

    override fun compareTo(other: Crash): Int = comparator.compare(this, other)

    companion object {
        private val comparator by lazy { compareBy(Crash::intensity, Crash::timestamp) }
    }
}

/**
 * Information of Crash location
 */
@Suppress("UnusedPrivateMember")
@Serializable
data class CrashLocation(
    @SerialName("datetime")
    @Serializable(with = InstantSerializer::class) private val dateTime: Instant? = null,
    @SerialName("lat") private val latitude: Float = 0f,
    @SerialName("lon") private val longitude: Float = 0f,
    @SerialName("alt") private val altitude: Float = 0f,
    @SerialName("accy") private val accuracy: Float = 0f,
    @SerialName("hdg") private val heading: Float = 0f
)

/**
 * Information of user acknowledgement on crash
 */
@Suppress("UnusedPrivateMember")
@Serializable
internal data class CrashResponse(
    @SerialName("user_verified") private val verified: Boolean = false,
    @Serializable(with = InstantSerializer::class)
    @SerialName("user_verified_datetime") private val verificationDateTime: Instant? = null
)
