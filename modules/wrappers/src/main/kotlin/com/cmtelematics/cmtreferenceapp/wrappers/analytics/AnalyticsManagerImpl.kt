package com.cmtelematics.cmtreferenceapp.wrappers.analytics

import android.media.AudioManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.SplashRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.ExistingUserEmailRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.RegisterEmailRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.authentication.VerifyCodeRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceCancelledRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceRequestedRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashAssistRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedCountDownRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedNegativeRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.DashboardRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.HomeRoute
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
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.DebugMenuRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.SettingsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.CreateVehicleRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.LinkTagRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.TagLinkedRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleDetailsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleListRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripDetailsRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripListRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.ActivityPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.ActivityTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.AmbulanceCancelled
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.AmbulanceRequested
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BackgroundLocationPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BackgroundLocationTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BatteryOptimizationPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BatteryOptimizationTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BluetoothDisabledTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BluetoothPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.BluetoothTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.CrashAssist
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.CrashDetectedCountDown
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.CrashDetectedNegative
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.CreateVehicle
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.DebugMenu
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.ExistingUserEmail
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.FineAndBackgroundLocationTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.FineLocationTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.GpsTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.Home
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.LinkTag
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.NotificationPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.PreciseLocationPermission
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.PreciseLocationTrapDoor
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.Splash
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.TagLinked
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.VehicleDetails
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.VehicleList
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.VerifyCode
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AppAnalyticsScreen.Welcome
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.util.getAlertAnalyticsCategory
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.util.getDevicePlaybackLabel
import com.cmtelematics.sdk.AppAnalyticsManager
import com.cmtelematics.sdk.types.AppAnalyticsScreen
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.LOCATION_PERMISSION
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.OVERVIEW
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.REGISTRATION
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.SETTINGS
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.TRIPLIST
import com.cmtelematics.sdk.types.AppAnalyticsScreenDefault.TRIP_DETAILS
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class AnalyticsManagerImpl @Inject constructor(
    private val sdkAnalyticsManager: AppAnalyticsManager,
    private val dispatcherProvider: DispatcherProvider,
    private val audioManager: AudioManager
) : AnalyticsManager {

    override suspend fun logScreenNavigation(
        previousScreen: Route?,
        currentScreen: Route?
    ) = withContext(dispatcherProvider.main) {
        val previousAnalyticsScreen = previousScreen?.mapToSdkAnalyticsScreen()
        val currentAnalyticsScreen = currentScreen?.mapToSdkAnalyticsScreen()

        previousAnalyticsScreen?.let { sdkAnalyticsManager.logScreenDidDisappear(it) }
        currentAnalyticsScreen?.let { sdkAnalyticsManager.logScreenDidAppear(it) }

        Timber.i(
            "Analytics event triggered for the following analytics keys: " +
                "(prev screen) $previousAnalyticsScreen, (current screen) $currentAnalyticsScreen"
        )
    }

    override suspend fun logAlertSetting(
        eventIdentifier: String,
        alertType: AlertType,
        enabled: Boolean
    ) = withContext(dispatcherProvider.main) {
        logAlertAnalyticsEvent(
            category = alertType.getAlertAnalyticsCategory(),
            action = ALERTS_SETTING_ACTION,
            label = if (enabled) {
                ALERTS_ENABLED
            } else {
                ALERTS_DISABLED
            },
            identifier = eventIdentifier
        )
    }

    override suspend fun logAlertPlayback(
        eventIdentifier: String,
        alertType: AlertType,
        enabled: Boolean
    ) = withContext(dispatcherProvider.main) {
        logAlertAnalyticsEvent(
            category = alertType.getAlertAnalyticsCategory(),
            action = ALERTS_PLAYBACK_ACTION,
            label = if (enabled) {
                getDevicePlaybackLabel(audioManager)
            } else {
                ALERTS_DISABLED
            },
            identifier = eventIdentifier
        )
    }

    /**
     * Logs a custom analytics event.
     *
     * @param category The category of the event.
     * @param action The action that triggered this event.
     * @param label The label associated with this event.
     * @param identifier The identifier associated with this event.
     */
    private suspend fun logAlertAnalyticsEvent(
        category: String,
        action: String,
        label: String,
        identifier: String
    ) = withContext(dispatcherProvider.main) {
        sdkAnalyticsManager.logCustomEvent(category, action, label, null, identifier)
        Timber.i(
            "Custom analytics event triggered for the following analytics keys: " +
                "(category) $category, (action) $action, (label) $label, (identifier) $identifier"
        )
    }

    @Suppress("ComplexMethod")
    private fun Route.mapToSdkAnalyticsScreen(): AppAnalyticsScreen = when (this) {
        is ExistingUserEmailRoute -> ExistingUserEmail
        is RegisterEmailRoute -> REGISTRATION
        is VerifyCodeRoute -> VerifyCode
        is DashboardRoute -> OVERVIEW
        is HomeRoute -> Home
        is ActivityPermissionRoute -> ActivityPermission
        is ActivityTrapDoorRoute -> ActivityTrapDoor
        is BackgroundLocationPermissionRoute -> BackgroundLocationPermission
        is BackgroundLocationTrapDoorRoute -> BackgroundLocationTrapDoor
        is BatteryOptimizationPermissionRoute -> BatteryOptimizationPermission
        is BatteryOptimizationTrapDoorRoute -> BatteryOptimizationTrapDoor
        is BluetoothPermissionRoute -> BluetoothPermission
        is BluetoothTrapDoorRoute -> BluetoothTrapDoor
        is BluetoothDisabledTrapDoorRoute -> BluetoothDisabledTrapDoor
        is FineAndBackgroundLocationPermissionRoute -> LOCATION_PERMISSION
        is FineAndBackgroundLocationTrapDoorRoute -> FineAndBackgroundLocationTrapDoor
        is FineLocationPermissionRoute -> LOCATION_PERMISSION
        is FineLocationTrapDoorRoute -> FineLocationTrapDoor
        is GpsTrapDoorRoute -> GpsTrapDoor
        is PreciseLocationPermissionRoute -> PreciseLocationPermission
        is OnlyPreciseLocationPermissionRoute -> PreciseLocationPermission
        is PreciseAndBackgroundLocationTrapDoorRoute -> PreciseLocationTrapDoor
        is NotificationPermissionRoute -> NotificationPermission
        is DebugMenuRoute -> DebugMenu
        is SettingsRoute -> SETTINGS
        is CreateVehicleRoute -> CreateVehicle
        is LinkTagRoute -> LinkTag
        is TagLinkedRoute -> TagLinked
        is VehicleDetailsRoute -> VehicleDetails
        is VehicleListRoute -> VehicleList
        is TripDetailsRoute -> TRIP_DETAILS
        is TripListRoute -> TRIPLIST
        is WelcomeRoute -> Welcome
        is SplashRoute -> Splash
        is CrashAssistRoute -> CrashAssist
        is CrashDetectedCountDownRoute -> CrashDetectedCountDown
        is AmbulanceRequestedRoute -> AmbulanceRequested
        is CrashDetectedNegativeRoute -> CrashDetectedNegative
        is AmbulanceCancelledRoute -> AmbulanceCancelled
        else -> error("Unsupported Route type: ${this::class.java.name}")
    }

    private companion object {
        private const val ALERTS_SETTING_ACTION = "alert_setting"
        private const val ALERTS_PLAYBACK_ACTION = "alert_playback"
        private const val ALERTS_DISABLED = "disabled"
        private const val ALERTS_ENABLED = "enabled"
    }
}
