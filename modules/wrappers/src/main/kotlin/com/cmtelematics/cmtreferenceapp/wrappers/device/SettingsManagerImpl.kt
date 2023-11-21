package com.cmtelematics.cmtreferenceapp.wrappers.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.util.Constants.isTripSuppressionNotificationEnabled
import com.cmtelematics.sdk.StandbyModeManager
import com.cmtelematics.sdk.types.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.Clock
import java.time.Instant
import javax.inject.Inject
import com.cmtelematics.sdk.DeviceSettingsManager as SdkDeviceSettingsManager

/**
 * [Docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/integrating-the-drivewell-sdk--android-/enabling-push-notifications--android-.html)
 * [Wifi docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/integrating-the-drivewell-sdk--android-/enabling-core-features--android-.html)
 */
internal class SettingsManagerImpl @Inject constructor(
    private val deviceSettingsManager: SdkDeviceSettingsManager,
    private val appConfiguration: Configuration,
    private val dispatcherProvider: DispatcherProvider,
    @SessionDataStore
    private val preferences: DataStore<Preferences>,
    private val standbyModeManager: StandbyModeManager,
    private val clock: Clock
) : SettingsManager {
    private val mutablePushToken = MutableStateFlow<String?>(null)

    override val pushToken: StateFlow<String?> = mutablePushToken.asStateFlow()

    override val standbyExpirationDate = MutableStateFlow<Instant?>(null)

    override suspend fun getTripCompletionNotificationsEnabled(): Boolean = withContext(dispatcherProvider.io) {
        deviceSettingsManager.isNotifyOnNewResults
    }

    override suspend fun setTripCompletionNotificationsEnabled(enabled: Boolean) = withContext(dispatcherProvider.io) {
        deviceSettingsManager.isNotifyOnNewResults = enabled
    }

    override fun setFirebaseToken(token: String) {
        deviceSettingsManager.setFirebaseId(token)
        mutablePushToken.value = token
    }

    override suspend fun getUploadOnWifiOnlyEnabled() = withContext(dispatcherProvider.io) {
        appConfiguration.isUploadOnWifiOnly
    }

    override suspend fun setUploadOnWifiOnlyEnabled(onlyWifi: Boolean) = withContext(dispatcherProvider.io) {
        appConfiguration.isUploadOnWifiOnly = onlyWifi
    }

    override suspend fun setTripSuppressionNotificationsEnabled(enabled: Boolean) {
        preferences.edit { it[isTripSuppressionNotificationEnabled] = enabled }
    }

    override suspend fun setStandByModeDuration(standByTimeInMinutes: Int) = withContext(dispatcherProvider.io) {
        standbyModeManager.setStandbyMinutes(standByTimeInMinutes)
    }

    override suspend fun refreshStandbyMode() = withContext(dispatcherProvider.io) {
        standbyExpirationDate.value = Instant.ofEpochMilli(standbyModeManager.standbyEndTime)
            .takeIf { expirationDate ->
                standbyModeManager.isInStandby && expirationDate.isAfter(clock.instant())
            }
    }
}
