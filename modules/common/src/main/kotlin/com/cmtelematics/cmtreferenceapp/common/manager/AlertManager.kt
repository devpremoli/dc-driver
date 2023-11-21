package com.cmtelematics.cmtreferenceapp.common.manager

import androidx.annotation.RawRes
import kotlinx.coroutines.flow.Flow

interface AlertManager {
    /**
     * Holds the current state of the real time alerts as an observable stream.
     *
     * This is a cached state, which gets refreshed by the [enableAudioAlerts] method call.
     */
    val state: Flow<Map<AlertType, Boolean>>

    /**
     * Initialization method for the AlertManager.
     *
     * @param[alerts] the list of supported [AlertType] and their [RawRes] audio file part.
     */
    suspend fun initialize(alerts: Map<AlertType, Int>)

    /**
     * Plays the alert associated with the given [alertType].
     */
    suspend fun playAlert(alertType: AlertType)

    /**
     * Stops all audio related processes.
     */
    suspend fun stop()

    /**
     * Enables or disables the [alertTypes] to the given [enabled] state.
     */
    suspend fun enableAudioAlerts(alertTypes: Array<AlertType> = AlertType.values(), enabled: Boolean)
}

/**
 * Represents supported alert types.
 */
enum class AlertType {
    HardBraking
}
