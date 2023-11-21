package com.cmtelematics.cmtreferenceapp.driver

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.device.DeviceManager
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HardwareRequirementsDriverImpl @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val deviceManager: DeviceManager,
    private val errorService: ErrorService
) : HardwareRequirementsDriver {
    override suspend fun run() {
        authenticationManager.state
            .filterIsInstance<LoggedIn>()
            .conflate()
            .collect { checkRequirements() }
    }

    private suspend fun checkRequirements() {
        val isAppOnlyUser = authenticationManager.profile.map { it?.isTagUser != true }.first()
        if (isAppOnlyUser && !deviceManager.hasGyro) {
            errorService.handle(HardwareRequirementsDriver.NoGyroInAppOnlyModeException())
        }
    }
}
