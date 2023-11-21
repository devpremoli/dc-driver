package com.cmtelematics.cmtreferenceapp.wrappers.analytics.util

import android.media.AudioDeviceInfo.TYPE_AUX_LINE
import android.media.AudioDeviceInfo.TYPE_BLE_HEADSET
import android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
import android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER
import android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER_SAFE
import android.media.AudioDeviceInfo.TYPE_WIRED_HEADPHONES
import android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET
import android.media.AudioManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType.HardBraking

private const val HARD_BRAKE_ALERTS_CATEGORY = "hardbrake_alerts"
private const val DEVICE_TYPE_HEADPHONES = "headphones"
private const val DEVICE_TYPE_SPEAKER_PHONES = "speakerPhone"
private const val DEVICE_TYPE_BLUETOOTH_A2DP = "bluetoothA2dp"
private const val DEVICE_TYPE_BLUETOOTH_HEADSET = "bluetoothHeadset"
private const val DEVICE_TYPE_ANDROID_AUTO = "androidAuto"
private const val DEVICE_TYPE_DEFAULT = "default"

internal fun AlertType.getAlertAnalyticsCategory() = if (this == HardBraking) {
    HARD_BRAKE_ALERTS_CATEGORY
} else {
    error("Unsupported alert type: $this")
}

internal fun getDevicePlaybackLabel(audioManager: AudioManager): String {
    val deviceTypes = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).map { it.type }

    return when {
        deviceTypes.any { it == TYPE_WIRED_HEADPHONES || it == TYPE_WIRED_HEADSET } -> DEVICE_TYPE_HEADPHONES
        deviceTypes.any { it == TYPE_BUILTIN_SPEAKER || it == TYPE_BUILTIN_SPEAKER_SAFE } -> DEVICE_TYPE_SPEAKER_PHONES
        deviceTypes.any { it == TYPE_BLUETOOTH_A2DP } -> DEVICE_TYPE_BLUETOOTH_A2DP
        deviceTypes.any { it == TYPE_BLE_HEADSET } -> DEVICE_TYPE_BLUETOOTH_HEADSET
        deviceTypes.any { it == TYPE_AUX_LINE } -> DEVICE_TYPE_ANDROID_AUTO
        else -> DEVICE_TYPE_DEFAULT
    }
}
