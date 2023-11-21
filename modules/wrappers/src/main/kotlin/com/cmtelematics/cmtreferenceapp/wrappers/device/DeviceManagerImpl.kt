package com.cmtelematics.cmtreferenceapp.wrappers.device

import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier.SharingScope
import com.cmtelematics.sdk.DeviceSettingsManager
import com.cmtelematics.sdk.types.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeviceManagerImpl @Inject constructor(
    authenticationManager: AuthenticationManager,
    configuration: Configuration,
    @SharingScope
    sharingScope: CoroutineScope,
    private val deviceSettingsManager: DeviceSettingsManager
) : DeviceManager {
    override val deviceIdentifier: Flow<String?> = authenticationManager.state
        .map { (it as? LoggedIn)?.let { configuration.deviceID } }
        .shareIn(scope = sharingScope, started = Lazily, replay = 1)

    override val hasGyro: Boolean
        get() = deviceSettingsManager.hasGyro()
}
