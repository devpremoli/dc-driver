package com.cmtelematics.cmtreferenceapp.wrappers.holder

import kotlinx.coroutines.flow.MutableStateFlow

internal interface WrapperBatteryIsLowForTripRecordingHolder {
    val batteryIsLowForTripRecording: MutableStateFlow<Boolean>
}
