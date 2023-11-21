package com.cmtelematics.cmtreferenceapp.wrappers.manager

import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManager
import com.cmtelematics.cmtreferenceapp.wrappers.driver.BatteryIsLowForTripRecordingDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.FirebaseIdDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.HardBrakeDetectorDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.LogoutDataStoreClearDriver
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class WrapperDriverManagerImpl @Inject constructor(
    @DriverScope
    private val driverScope: CoroutineScope,
    private val firebaseIdDriver: Lazy<FirebaseIdDriver>,
    private val crashNotificationManager: CrashNotificationManager,
    private val batteryIsLowForTripRecordingDriver: Lazy<BatteryIsLowForTripRecordingDriver>,
    private val logoutDataStoreClearDriver: Lazy<LogoutDataStoreClearDriver>,
    private val hardBrakeDetectorDriver: Lazy<HardBrakeDetectorDriver>
) : WrapperDriverManager {
    override fun launchDrivers() {
        driverScope.launch { firebaseIdDriver.get().run() }

        driverScope.launch { crashNotificationManager.maintainNotificationScheduling() }

        driverScope.launch { batteryIsLowForTripRecordingDriver.get().run() }

        driverScope.launch { logoutDataStoreClearDriver.get().run() }

        driverScope.launch { hardBrakeDetectorDriver.get().run() }
    }
}
