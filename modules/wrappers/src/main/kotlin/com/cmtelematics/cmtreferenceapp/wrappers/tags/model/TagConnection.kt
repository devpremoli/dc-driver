package com.cmtelematics.cmtreferenceapp.wrappers.tags.model

sealed interface TagConnection {
    object Disconnected : TagConnection
    data class Connected(val macAddress: String) : TagConnection
}
