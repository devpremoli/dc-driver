package com.cmtelematics.cmtreferenceapp.wrappers.device

import com.cmtelematics.cmtreferenceapp.common.manager.StandbySettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant

/**
 * Manages device-specific SDK settings related to push notifications and hardware.
 */
@Suppress("ComplexInterface")
interface SettingsManager : StandbySettingsManager {
    /**
     * The latest value for the push token as set by [setFirebaseToken]. Only exposed for debugging purposes.
     */
    val pushToken: StateFlow<String?>

    /**
     * The current expiration date of the selected trip recording standby mode. Call [refreshStandbyMode] to update
     * from the persistent local storage.
     */
    override val standbyExpirationDate: Flow<Instant?>

    /**
     * Gets the current setting of trip results push notifications.
     */
    suspend fun getTripCompletionNotificationsEnabled(): Boolean

    /**
     * Enables or disables trip completion push notifications. This only updates the config locally. The changed
     * setting will be automatically synced with the backend as soon as possible.
     *
     * @param enabled the new value for the setting
     */
    suspend fun setTripCompletionNotificationsEnabled(enabled: Boolean)

    /**
     * Update the firebase token in the SDK. The changed token will be automatically synced with the backend as soon as
     * possible
     *
     * @param token the new value for the token
     */
    fun setFirebaseToken(token: String)

    /**
     * Returns the current state of the uploading way of data
     * (via Wifi connection only or mobile network allowed to use).
     */
    suspend fun getUploadOnWifiOnlyEnabled(): Boolean

    /**
     * Set that the data can be uploaded via Wifi connection or it can be with the usage of mobile network.
     */
    suspend fun setUploadOnWifiOnlyEnabled(onlyWifi: Boolean)

    /**
     * Set whether Android notifications about trip recording issues ("trip detection suppression") are enabled.
     */
    suspend fun setTripSuppressionNotificationsEnabled(enabled: Boolean)

    /**
     * Refreshes the standby mode state of trip recording.
     */
    override suspend fun refreshStandbyMode()

    /**
     * Set the trip recording's standby mode.
     *
     * @param standByTimeInMinutes the standby time of trip recording.
     */
    override suspend fun setStandByModeDuration(standByTimeInMinutes: Int)
}
