package com.cmtelematics.cmtreferenceapp.wrappers.driver

import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType.HardBraking
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManager
import com.cmtelematics.sdk.hardbrake.HardBrakeDetector
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HardBrakeDetectorDriverImpl @Inject constructor(
    private val hardBrakeDetector: HardBrakeDetector,
    private val alertManager: AlertManager,
    private val analyticsManager: AnalyticsManager,
    private val startupManager: StartupManager
) : HardBrakeDetectorDriver {
    override suspend fun run() {
        coroutineScope {
            val isHardBrakeAlertEnabled =
                startupManager.isAppInitialised()
                    .filter { it }
                    .flatMapLatest { isHardBrakeAlertEnabled() }
                    .stateIn(this, SharingStarted.Eagerly, false)

            callbackFlow {
                hardBrakeDetector.setListener { trySend(it) }
                awaitClose { hardBrakeDetector.setListener(null) }
            }.buffer()
                .collect { event ->
                    val isAlertEnabled = isHardBrakeAlertEnabled.value
                    if (isAlertEnabled) {
                        alertManager.playAlert(HardBraking)
                    }

                    analyticsManager.logAlertPlayback(event.identifier, HardBraking, isAlertEnabled)
                    analyticsManager.logAlertSetting(event.identifier, HardBraking, isAlertEnabled)
                }
        }
    }

    private fun isHardBrakeAlertEnabled(): Flow<Boolean> = alertManager.state
        .map { it[HardBraking] ?: error("$HardBraking alert type is not found in alert states!") }
}
