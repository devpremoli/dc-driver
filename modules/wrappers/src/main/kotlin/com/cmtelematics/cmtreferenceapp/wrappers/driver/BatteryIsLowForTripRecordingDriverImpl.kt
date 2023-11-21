package com.cmtelematics.cmtreferenceapp.wrappers.driver

import android.app.Activity
import android.app.Application
import com.cmtelematics.cmtreferenceapp.common.util.EmptyActivityLifecycleCallbacks
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperBatteryIsLowForTripRecordingHolder
import com.cmtelematics.sdk.TelematicsServiceManager
import com.cmtelematics.sdk.types.BatteryState
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

internal class BatteryIsLowForTripRecordingDriverImpl @Inject constructor(
    private val application: Application,
    private val serviceManager: TelematicsServiceManager,
    private val wrapperBatteryIsLowForTripRecordingHolder: WrapperBatteryIsLowForTripRecordingHolder
) : BatteryIsLowForTripRecordingDriver {

    override suspend fun run() {
        callbackFlow {
            application.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {

                override fun onActivityResumed(activity: Activity) {
                    serviceManager.loadBatteryState { batteryState ->
                        trySend(batteryState == BatteryState.LOW)
                    }
                }
            })

            awaitCancellation()
        }
            .buffer()
            .collect { wrapperBatteryIsLowForTripRecordingHolder.batteryIsLowForTripRecording.value = it }
    }
}
