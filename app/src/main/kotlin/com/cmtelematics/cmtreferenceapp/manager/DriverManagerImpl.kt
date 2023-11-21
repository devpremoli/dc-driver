package com.cmtelematics.cmtreferenceapp.manager

import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.common.manager.DriverManager
import com.cmtelematics.cmtreferenceapp.driver.CrashDetectionDriver
import com.cmtelematics.cmtreferenceapp.driver.HardwareRequirementsDriver
import com.cmtelematics.cmtreferenceapp.driver.LogoutNavigationDriver
import com.cmtelematics.cmtreferenceapp.driver.PermissionChangeNotifierDriver
import com.cmtelematics.cmtreferenceapp.driver.ScreenResumeTrapdoorsDriver
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DriverManagerImpl @Inject constructor(
    @DriverScope
    private val driverScope: CoroutineScope,
    private val hardwareRequirementsDriver: Lazy<HardwareRequirementsDriver>,
    private val logoutNavigationDriver: Lazy<LogoutNavigationDriver>,
    private val crashDetectionDriver: Lazy<CrashDetectionDriver>,
    private val permissionChangeNotifierDriver: Lazy<PermissionChangeNotifierDriver>,
    private val screenResumeTrapdoorsDriver: Lazy<ScreenResumeTrapdoorsDriver>
) : DriverManager {
    override fun launchDrivers() {
        driverScope.launch {
            hardwareRequirementsDriver.get().run()
        }

        driverScope.launch {
            logoutNavigationDriver.get().run()
        }

        driverScope.launch {
            crashDetectionDriver.get().run()
        }

        driverScope.launch {
            permissionChangeNotifierDriver.get().run()
        }

        driverScope.launch {
            screenResumeTrapdoorsDriver.get().run()
        }
    }
}
