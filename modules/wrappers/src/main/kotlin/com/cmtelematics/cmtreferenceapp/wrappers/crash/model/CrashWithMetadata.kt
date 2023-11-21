package com.cmtelematics.cmtreferenceapp.wrappers.crash.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Extra fields for crash wrapped into an immutable data structure.
 */
@Serializable
internal data class CrashWithMetadata(
    val crash: Crash,
    @Serializable(with = InstantSerializer::class)
    val firstNotificationTime: Instant? = null,
    val flowStarted: Boolean = false
)
