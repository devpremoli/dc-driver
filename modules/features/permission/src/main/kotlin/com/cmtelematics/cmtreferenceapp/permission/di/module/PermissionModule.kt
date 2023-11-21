package com.cmtelematics.cmtreferenceapp.permission.di.module

import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.PermanentDataStore
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.permission.manager.Android10PermissionManager
import com.cmtelematics.cmtreferenceapp.permission.manager.Android11PermissionManager
import com.cmtelematics.cmtreferenceapp.permission.manager.Android12PermissionManager
import com.cmtelematics.cmtreferenceapp.permission.manager.Android13PermissionManager
import com.cmtelematics.cmtreferenceapp.permission.manager.Android9OrBelowPermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PermissionModule {

    @Suppress("LongParameterList")
    @Provides
    @Singleton
    fun providePermissionManager(
        @ApplicationContext
        applicationContext: Context,
        locationManager: LocationManager,
        powerManager: PowerManager,
        @PermanentDataStore
        preferences: DataStore<Preferences>,
        dispatcherProvider: DispatcherProvider,
        bluetoothManager: BluetoothManager
    ): PermissionManager = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> Android13PermissionManager(
            applicationContext = applicationContext,
            locationManager = locationManager,
            powerManager = powerManager,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            bluetoothManager = bluetoothManager
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> Android12PermissionManager(
            applicationContext = applicationContext,
            locationManager = locationManager,
            powerManager = powerManager,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            bluetoothManager = bluetoothManager
        )
        Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> Android11PermissionManager(
            applicationContext = applicationContext,
            locationManager = locationManager,
            powerManager = powerManager,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            bluetoothManager = bluetoothManager
        )
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> Android10PermissionManager(
            applicationContext = applicationContext,
            locationManager = locationManager,
            powerManager = powerManager,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            bluetoothManager = bluetoothManager
        )
        else -> Android9OrBelowPermissionManager(
            applicationContext = applicationContext,
            locationManager = locationManager,
            powerManager = powerManager,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            bluetoothManager = bluetoothManager
        )
    }
}
