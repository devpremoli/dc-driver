package com.cmtelematics.cmtreferenceapp.navigation.route.permission

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.route.BaseOnboardingRoute
import kotlinx.parcelize.Parcelize

@Parcelize
class BluetoothDisabledTrapDoorRoute(
    override val shouldReturnToOriginalScreenOnFinish: Boolean
) : BaseOnboardingRoute(this, shouldReturnToOriginalScreenOnFinish) {

    companion object : Factory<BluetoothDisabledTrapDoorRoute>() {

        override val path = "bluetoothDisabledTrapDoor"

        override fun create(bundle: Bundle?) = BluetoothDisabledTrapDoorRoute(
            shouldReturnToOriginalScreenOnFinish = bundle?.run {
                getBoolean(ARG_SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW)
            } ?: error("shouldReturnToOriginalScreenOnFinish flag was not provided in route!")
        )
    }
}
