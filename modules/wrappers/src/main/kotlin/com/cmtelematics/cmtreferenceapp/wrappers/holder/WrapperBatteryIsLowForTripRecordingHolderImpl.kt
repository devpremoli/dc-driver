package com.cmtelematics.cmtreferenceapp.wrappers.holder

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class WrapperBatteryIsLowForTripRecordingHolderImpl @Inject constructor() :
    WrapperBatteryIsLowForTripRecordingHolder {
    override val batteryIsLowForTripRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)
}
