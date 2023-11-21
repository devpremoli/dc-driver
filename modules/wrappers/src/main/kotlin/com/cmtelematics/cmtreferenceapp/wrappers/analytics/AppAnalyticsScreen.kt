package com.cmtelematics.cmtreferenceapp.wrappers.analytics

import com.cmtelematics.sdk.types.AppAnalyticsScreen

internal enum class AppAnalyticsScreen : AppAnalyticsScreen {
    ExistingUserEmail {
        override fun getCategory() = "cmt_return_login_email"
    },
    VerifyCode {
        override fun getCategory() = "cmt_verify_code"
    },
    Home {
        override fun getCategory() = "cmt_home"
    },
    ActivityPermission {
        override fun getCategory() = "cmt_activity_permission"
    },
    ActivityTrapDoor {
        override fun getCategory() = "cmt_activity_trapdoor"
    },
    BackgroundLocationPermission {
        override fun getCategory() = "cmt_background_location_permission"
    },
    BackgroundLocationTrapDoor {
        override fun getCategory() = "cmt_background_location_trapdoor"
    },
    BatteryOptimizationPermission {
        override fun getCategory() = "cmt_battery_optimization_permission"
    },
    BatteryOptimizationTrapDoor {
        override fun getCategory() = "cmt_battery_optimization_trapdoor"
    },
    BluetoothPermission {
        override fun getCategory() = "cmt_bluetooth_permission"
    },
    BluetoothTrapDoor {
        override fun getCategory() = "cmt_bluetooth_trapdoor"
    },
    BluetoothDisabledTrapDoor {
        override fun getCategory() = "cmt_bluetooth_disabled_trapdoor"
    },
    FineAndBackgroundLocationTrapDoor {
        override fun getCategory() = "cmt_fine_and_background_location_trapdoor"
    },
    FineLocationTrapDoor {
        override fun getCategory() = "cmt_fine_location_trapdoor"
    },
    GpsTrapDoor {
        override fun getCategory() = "cmt_gps_trapdoor"
    },
    PreciseLocationPermission {
        override fun getCategory() = "cmt_precise_location_permission"
    },
    PreciseLocationTrapDoor {
        override fun getCategory() = "cmt_precise_location_trapdoor"
    },
    NotificationPermission {
        override fun getCategory() = "cmt_notification_permission"
    },
    DebugMenu {
        override fun getCategory() = "cmt_debug_menu"
    },
    CreateVehicle {
        override fun getCategory() = "cmt_add_vehicle"
    },
    LinkTag {
        override fun getCategory() = "cmt_tag_linking"
    },
    TagLinked {
        override fun getCategory() = "cmt_link_tag_success"
    },
    VehicleDetails {
        override fun getCategory() = "cmt_vehicle_detail"
    },
    VehicleList {
        override fun getCategory() = "cmt_vehicles"
    },
    Welcome {
        override fun getCategory() = "cmt_welcome"
    },
    Splash {
        override fun getCategory() = "cmt_splash"
    },
    CrashAssist {
        override fun getCategory() = "cmt_crash_assist"
    },
    CrashDetectedCountDown {
        override fun getCategory() = "cmt_crash_detected_count_down"
    },
    AmbulanceRequested {
        override fun getCategory() = "cmt_ambulance_requested"
    },
    CrashDetectedNegative {
        override fun getCategory() = "cmt_crash_detected_negative"
    },
    AmbulanceCancelled {
        override fun getCategory() = "cmt_ambulance_cancelled"
    }
}
