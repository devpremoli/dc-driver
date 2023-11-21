package com.cmtelematics.cmtreferenceapp.common.mapper

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.os.Build
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.sdk.types.ServiceConstants.ACTION_ACTIVITY_RECOGNITION_PERMISSION_CHANGED
import com.cmtelematics.sdk.types.ServiceConstants.ACTION_BLUETOOTH_PERMISSION_CHANGED
import com.cmtelematics.sdk.types.ServiceConstants.ACTION_IGNORE_BATTERY_OPTIMIZATION_CHANGED
import com.cmtelematics.sdk.types.ServiceConstants.ACTION_LOCATION_PERMISSION_CHANGED
import com.cmtelematics.sdk.types.ServiceConstants.ACTION_POST_NOTIFICATIONS_PERMISSION_CHANGED

@SuppressLint("InlinedApi")
fun PermissionType.toPermissions(): Array<String> = when (this) {
    PermissionType.FineLocation -> arrayOf(ACCESS_FINE_LOCATION)
    PermissionType.PreciseLocation -> arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    PermissionType.OnlyPreciseLocation -> arrayOf(ACCESS_FINE_LOCATION)
    PermissionType.BackgroundLocation -> arrayOf(ACCESS_BACKGROUND_LOCATION)
    PermissionType.FineAndBackgroundLocation -> arrayOf(ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION)
    PermissionType.Bluetooth -> if (Build.VERSION_CODES.S <= Build.VERSION.SDK_INT) {
        arrayOf(BLUETOOTH_CONNECT, BLUETOOTH_SCAN)
    } else {
        arrayOf(BLUETOOTH, BLUETOOTH_ADMIN)
    }
    PermissionType.PhysicalActivity -> arrayOf(ACTIVITY_RECOGNITION)
    PermissionType.Notification -> arrayOf(POST_NOTIFICATIONS)
    else -> emptyArray() // The remaining PermissionType-s have custom permission request implementation
}

val PermissionType.permissionChangedAction: String?
    get() = when (this) {
        PermissionType.FineLocation,
        PermissionType.PreciseLocation,
        PermissionType.OnlyPreciseLocation,
        PermissionType.BackgroundLocation,
        PermissionType.FineAndBackgroundLocation -> ACTION_LOCATION_PERMISSION_CHANGED
        PermissionType.Bluetooth -> ACTION_BLUETOOTH_PERMISSION_CHANGED
        PermissionType.PhysicalActivity -> ACTION_ACTIVITY_RECOGNITION_PERMISSION_CHANGED
        PermissionType.BatteryOptimization -> ACTION_IGNORE_BATTERY_OPTIMIZATION_CHANGED
        PermissionType.Notification -> ACTION_POST_NOTIFICATIONS_PERMISSION_CHANGED
        else -> null
    }
