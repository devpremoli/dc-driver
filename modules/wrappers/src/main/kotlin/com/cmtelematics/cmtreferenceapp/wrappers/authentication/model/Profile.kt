package com.cmtelematics.cmtreferenceapp.wrappers.authentication.model

import kotlinx.serialization.Serializable

/**
 * Local model class for the user's Profile.
 */
@Serializable
data class Profile(
    val isTagUser: Boolean = false,
    val email: String? = null,
    val crashAssistEnabled: Boolean = false
)
