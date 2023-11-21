package com.cmtelematics.cmtreferenceapp.permission.factory

import android.os.Build
import com.cmtelematics.cmtreferenceapp.common.factory.PermissionRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState.Allowed
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState.PartiallyAllowed
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState.TrapDoor
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState.Undetermined
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.BackgroundLocation
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.BatteryOptimization
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.Bluetooth
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.BluetoothAdapter
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.FineAndBackgroundLocation
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.FineLocation
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.HighAccuracy
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.Notification
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.OnlyPreciseLocation
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.PhysicalActivity
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.PreciseLocation
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.ActivityPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.ActivityTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BackgroundLocationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BackgroundLocationTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BatteryOptimizationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BatteryOptimizationTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BluetoothDisabledTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BluetoothPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.BluetoothTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.FineAndBackgroundLocationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.FineAndBackgroundLocationTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.FineLocationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.FineLocationTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.GpsTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.NotificationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.OnlyPreciseLocationPermissionRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.PreciseAndBackgroundLocationTrapDoorRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.PreciseLocationPermissionRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class PermissionRouteFactoryImpl @Inject constructor(
    private val permissionManager: PermissionManager,
    private val authenticationManager: AuthenticationManager,
    private val dispatcherProvider: DispatcherProvider
) : PermissionRouteFactory {

    override suspend fun create(shouldReturnToOriginalScreenOnFinish: Boolean) =
        withContext(dispatcherProvider.default) {
            getPermissionRoute(shouldReturnToOriginalScreenOnFinish)
        }

    private suspend fun getPermissionRoute(shouldReturnToOriginalScreenOnFinish: Boolean): Route? {
        permissionManager.refresh()

        val isTagUser = authenticationManager.profile.map { it?.isTagUser == true }.first()

        val selectedRoute: Route? = permissionManager.state
            .onEach { list ->
                Timber.d("Current state of the required app permissions (PermissionType, PermissionState): $list")
            }
            .map { currentStateList ->
                val nextDisallowedPermission = currentStateList
                    .filter { (permissionType, permissionState) ->
                        when (permissionType) {
                            Bluetooth -> isTagUser
                            BluetoothAdapter -> isTagUser
                            Notification -> permissionState == Undetermined
                            else -> true
                        }
                    }
                    .firstOrNull { (_, permissionState) ->
                        permissionState !in listOf(Allowed, PartiallyAllowed)
                    }

                nextDisallowedPermission?.let { (permissionType, permissionState) ->
                    mapPermissionsToRoute(permissionType, permissionState, shouldReturnToOriginalScreenOnFinish)
                }
            }
            .onEach { route ->
                Timber.d(
                    "The navigation route of next missing permission (null if every app permission accepted): $route"
                )
            }
            .first()

        return selectedRoute
    }

    private fun mapPermissionsToRoute(
        permissionType: PermissionType,
        permissionState: PermissionState,
        shouldReturnToOriginalScreenOnFinish: Boolean
    ): Route = when (permissionType) {
        PreciseLocation -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = PreciseLocationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = PreciseAndBackgroundLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        OnlyPreciseLocation -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = OnlyPreciseLocationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = PreciseAndBackgroundLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        FineLocation -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = FineLocationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = FineLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        FineAndBackgroundLocation -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = FineAndBackgroundLocationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = FineAndBackgroundLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )

        BackgroundLocation ->
            getRouteByPermissionState(
                permissionState = permissionState,
                permissionRoute = BackgroundLocationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
                trapdoorRoute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PreciseAndBackgroundLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
                } else {
                    BackgroundLocationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
                }
            )
        HighAccuracy -> GpsTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        PhysicalActivity -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = ActivityPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = ActivityTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        Bluetooth -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = BluetoothPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = BluetoothTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        BluetoothAdapter -> BluetoothDisabledTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        BatteryOptimization -> getRouteByPermissionState(
            permissionState = permissionState,
            permissionRoute = BatteryOptimizationPermissionRoute(shouldReturnToOriginalScreenOnFinish),
            trapdoorRoute = BatteryOptimizationTrapDoorRoute(shouldReturnToOriginalScreenOnFinish)
        )
        Notification -> NotificationPermissionRoute(shouldReturnToOriginalScreenOnFinish)
    }

    private fun getRouteByPermissionState(
        permissionState: PermissionState,
        permissionRoute: Route,
        trapdoorRoute: Route
    ) = if (permissionState == TrapDoor) {
        trapdoorRoute
    } else {
        permissionRoute
    }
}
