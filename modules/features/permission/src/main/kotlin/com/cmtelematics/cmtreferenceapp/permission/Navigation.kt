package com.cmtelematics.cmtreferenceapp.permission

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
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
import com.cmtelematics.cmtreferenceapp.permission.ui.ActivityPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.ActivityTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BackgroundLocationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BackgroundLocationTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BatteryOptimizationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BatteryOptimizationTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BluetoothDisabledTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BluetoothPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.BluetoothTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.FineAndBackgroundLocationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.FineAndBackgroundLocationTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.FineLocationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.FineLocationTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.GpsTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.NotificationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.OnlyPreciseLocationPermissionScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.PreciseAndBackgroundLocationTrapDoorScreen
import com.cmtelematics.cmtreferenceapp.permission.ui.PreciseLocationPermissionScreen

fun NavGraphBuilder.permissionNavigation() {
    // Permissions
    composable(FineLocationPermissionRoute) { FineLocationPermissionScreen() }
    composable(BackgroundLocationPermissionRoute) { BackgroundLocationPermissionScreen() }
    composable(FineAndBackgroundLocationPermissionRoute) { FineAndBackgroundLocationPermissionScreen() }
    composable(PreciseLocationPermissionRoute) { PreciseLocationPermissionScreen() }
    composable(OnlyPreciseLocationPermissionRoute) { OnlyPreciseLocationPermissionScreen() }
    composable(ActivityPermissionRoute) { ActivityPermissionScreen() }
    composable(BluetoothPermissionRoute) { BluetoothPermissionScreen() }
    composable(BatteryOptimizationPermissionRoute) { BatteryOptimizationPermissionScreen() }
    composable(NotificationPermissionRoute) { NotificationPermissionScreen() }

    // Trapdoors
    composable(FineLocationTrapDoorRoute) { FineLocationTrapDoorScreen() }
    composable(BackgroundLocationTrapDoorRoute) { BackgroundLocationTrapDoorScreen() }
    composable(FineAndBackgroundLocationTrapDoorRoute) { FineAndBackgroundLocationTrapDoorScreen() }
    composable(PreciseAndBackgroundLocationTrapDoorRoute) { PreciseAndBackgroundLocationTrapDoorScreen() }
    composable(ActivityTrapDoorRoute) { ActivityTrapDoorScreen() }
    composable(BluetoothTrapDoorRoute) { BluetoothTrapDoorScreen() }
    composable(BluetoothDisabledTrapDoorRoute) { BluetoothDisabledTrapDoorScreen() }
    composable(GpsTrapDoorRoute) { GpsTrapDoorScreen() }
    composable(BatteryOptimizationTrapDoorRoute) { BatteryOptimizationTrapDoorScreen() }
}
