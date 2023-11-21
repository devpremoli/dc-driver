package com.cmtelematics.cmtreferenceapp.permission.manager

import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.util.isPartiallyAllowed
import com.cmtelematics.cmtreferenceapp.common.util.isPermissionAllowed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Locale

internal abstract class BasePermissionManager(
    private val applicationContext: Context,
    private val locationManager: LocationManager,
    private val powerManager: PowerManager,
    private val preferences: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider,
    private val bluetoothManager: BluetoothManager
) : PermissionManager {

    private val mutableState = MutableStateFlow<List<Pair<PermissionType, PermissionState>>?>(null)

    override val state: Flow<List<Pair<PermissionType, PermissionState>>> = mutableState.filterNotNull()

    abstract val requiredPermissions: List<PermissionType>

    override suspend fun refresh() = withContext(dispatcherProvider.default) {
        val permissionTypesWithStates = requiredPermissions.map { permissionType ->
            permissionType to if (permissionType == PermissionType.PreciseLocation) {
                getPermissionStateOfPartiallyAcceptablePermission(permissionType)
            } else {
                getPermissionStateOf(permissionType)
            }
        }
        mutableState.value = permissionTypesWithStates
    }

    override suspend fun markAsRequested(permissionType: PermissionType) {
        val preferenceKey = booleanPreferencesKey(permissionType.name)

        preferences.edit { it[preferenceKey] = true }
    }

    private suspend fun hasPermissionPromptShownBefore(permissionType: PermissionType): Boolean {
        val preferenceKey = booleanPreferencesKey(permissionType.name)

        return preferences.data.map { it[preferenceKey] == true }.first()
    }

    private suspend fun getPermissionStateOf(permissionType: PermissionType): PermissionState {
        val isPermissionAllowed = when (permissionType) {
            PermissionType.HighAccuracy -> isGpsAllowed()
            PermissionType.FineLocation,
            PermissionType.OnlyPreciseLocation,
            PermissionType.BackgroundLocation,
            PermissionType.FineAndBackgroundLocation,
            PermissionType.PhysicalActivity,
            PermissionType.Bluetooth,
            PermissionType.Notification -> isPermissionAllowed(applicationContext, permissionType)
            PermissionType.BluetoothAdapter -> isBluetoothEnabled()
            PermissionType.BatteryOptimization -> isBatteryOptimizationIgnored()
            else -> error("Unhandled permission: ${permissionType.name}")
        }
        val hasPromptShown = hasPermissionPromptShownBefore(permissionType)

        val state = when {
            !isPermissionAllowed && hasPromptShown -> PermissionState.TrapDoor
            !isPermissionAllowed -> PermissionState.Undetermined
            else -> PermissionState.Allowed
        }

        return state
    }

    private suspend fun getPermissionStateOfPartiallyAcceptablePermission(
        permissionType: PermissionType
    ): PermissionState {
        val hasPromptShown = hasPermissionPromptShownBefore(permissionType)
        val isPermissionAllowed = isPermissionAllowed(applicationContext, permissionType)
        val isPartiallyAllowed = isPartiallyAllowed(applicationContext, permissionType)
        return when {
            isPermissionAllowed -> PermissionState.Allowed
            isPartiallyAllowed -> PermissionState.PartiallyAllowed
            hasPromptShown -> PermissionState.TrapDoor
            else -> PermissionState.Undetermined
        }
    }

    private fun isBatteryOptimizationIgnored(): Boolean =
        if (Build.MANUFACTURER.uppercase(Locale.ENGLISH) == "SAMSUNG") {
            powerManager.isIgnoringBatteryOptimizations(applicationContext.packageName)
        } else {
            //  According to DriveWell SDK docs we should only request the user to remove our app from the Battery
            //  optimized list if the device is a SAMSUNG phone. In case of other manufacturers the application does
            //  not have to care about battery optimization.
            true
        }

    private fun isGpsAllowed(): Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun isBluetoothEnabled(): Boolean = bluetoothManager.adapter?.isEnabled == true
}
