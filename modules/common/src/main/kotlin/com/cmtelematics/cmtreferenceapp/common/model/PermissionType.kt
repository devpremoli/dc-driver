package com.cmtelematics.cmtreferenceapp.common.model

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.location.LocationManager

/**
 * Domain representation of Android specific permission types.
 */
enum class PermissionType {

    /**
     * Represents [ACCESS_FINE_LOCATION] system permission.
     */
    FineLocation,

    /**
     * Represents [ACCESS_COARSE_LOCATION] and [ACCESS_FINE_LOCATION] system permissions together.
     */
    PreciseLocation,

    /**
     * Represents [ACCESS_FINE_LOCATION] system permission. It is responsible for allowing the
     * precise location permission in case, when fine location permission is already accepted. Used from API 31.
     */
    OnlyPreciseLocation,

    /**
     * Represents [ACCESS_BACKGROUND_LOCATION] system permission.
     */
    BackgroundLocation,

    /**
     * Represents [ACCESS_BACKGROUND_LOCATION] and [ACCESS_FINE_LOCATION] system permissions together.
     */
    FineAndBackgroundLocation,

    /**
     * Represents [ACTIVITY_RECOGNITION] system permission.
     */
    PhysicalActivity,

    /**
     * Represents [BLUETOOTH_CONNECT] and [BLUETOOTH_SCAN] system permissions in case of API 31 and above.
     * Below API 31 it represents [BLUETOOTH] and [BLUETOOTH_ADMIN] system permissions.
     */
    Bluetooth,

    /**
     * Represents the enabled state of the default Bluetooth Adapter.
     */
    BluetoothAdapter,

    /**
     * Represents the [LocationManager.GPS_PROVIDER] and [LocationManager.NETWORK_PROVIDER] system settings.
     */
    HighAccuracy,

    /**
     * Represents [REQUEST_IGNORE_BATTERY_OPTIMIZATIONS] system permission.
     */
    BatteryOptimization,

    /**
     * Represents [POST_NOTIFICATIONS] system permission.
     */
    Notification
}
