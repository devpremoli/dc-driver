package com.cmtelematics.cmtreferenceapp.permission.manager

import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.os.PowerManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider

internal class Android9OrBelowPermissionManager(
    applicationContext: Context,
    locationManager: LocationManager,
    powerManager: PowerManager,
    preferences: DataStore<Preferences>,
    dispatcherProvider: DispatcherProvider,
    bluetoothManager: BluetoothManager
) : BasePermissionManager(
    applicationContext = applicationContext,
    locationManager = locationManager,
    powerManager = powerManager,
    preferences = preferences,
    dispatcherProvider = dispatcherProvider,
    bluetoothManager = bluetoothManager
) {
    override val requiredPermissions = listOf(
        PermissionType.FineLocation,
        PermissionType.HighAccuracy,
        PermissionType.Bluetooth,
        PermissionType.BluetoothAdapter,
        PermissionType.BatteryOptimization
    )
}
