package com.cmtelematics.cmtreferenceapp.wrappers.authentication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for updating value of crash_escalation_opt_in.
 */
@Serializable
internal data class ExtraProfileFields(
    @SerialName("crash_escalation_opt_in") val crashAssistEnabled: Boolean = false
)
