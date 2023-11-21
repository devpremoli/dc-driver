@file:Suppress("Deprecation")

package com.cmtelematics.cmtreferenceapp.wrappers.trip

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier.SharingScope
import com.cmtelematics.sdk.CmtService
import com.cmtelematics.sdk.MockTripManager
import com.cmtelematics.sdk.TelematicsServiceManager
import com.cmtelematics.sdk.types.DriveStartStopMethod.MANUAL
import com.cmtelematics.sdk.types.RecordingLevel
import com.cmtelematics.sdk.types.RecordingLevel.HIGH
import com.cmtelematics.sdk.types.ServiceRunningState
import com.cmtelematics.sdk.types.ServiceRunningState.RUNNING
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RecordingManagerImpl @Inject constructor(
    private val mockTripManager: MockTripManager,
    serviceManager: TelematicsServiceManager,
    @SharingScope sharingScope: CoroutineScope,
    bus: Bus,
    private val dispatcherProvider: DispatcherProvider
) : RecordingManager {
    private val serviceRunning = MutableStateFlow(CmtService.isRunning())
    private val isRecording = MutableStateFlow(serviceManager.recordingLevel == HIGH)

    override val isRecordingInProgress: StateFlow<Boolean> =
        combine(serviceRunning, isRecording) { running, recording -> running && recording }
            .stateIn(sharingScope, Eagerly, false)

    init {
        bus.register(this)
    }

    override suspend fun startMockTripRecording(): Unit = withContext(dispatcherProvider.default) {
        mockTripManager.startService()
        // wait for service to start
        serviceRunning.filter { it }.first()
        mockTripManager.startMockDrive(MANUAL)
        // wait for recording to start
        isRecording.filter { it }.first()
    }

    override suspend fun stopMockTripRecording(): Unit = withContext(dispatcherProvider.default) {
        mockTripManager.stopMockDrive(MANUAL)
        // wait for recording to stop
        isRecording.filter { !it }.first()
        mockTripManager.stopService()
        // wait for service to stop
        serviceRunning.filter { !it }.first()
    }

    @Subscribe
    fun handleServiceStateChanged(state: ServiceRunningState) {
        serviceRunning.value = state == RUNNING
    }

    @Subscribe
    fun handleRecordingLevelChanged(level: RecordingLevel) {
        isRecording.value = level == HIGH
    }
}
