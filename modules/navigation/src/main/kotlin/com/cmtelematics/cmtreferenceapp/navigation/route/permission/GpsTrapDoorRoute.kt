package com.cmtelematics.cmtreferenceapp.navigation.route.permission

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.route.BaseOnboardingRoute
import kotlinx.parcelize.Parcelize

@Parcelize
class GpsTrapDoorRoute(
    override val shouldReturnToOriginalScreenOnFinish: Boolean
) : BaseOnboardingRoute(this, shouldReturnToOriginalScreenOnFinish) {

    companion object : Factory<GpsTrapDoorRoute>() {

        override val path = "gpsTrapDoor"

        override fun create(bundle: Bundle?) = GpsTrapDoorRoute(
            shouldReturnToOriginalScreenOnFinish = bundle?.run {
                getBoolean(ARG_SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW)
            } ?: error("shouldReturnToOriginalScreenOnFinish flag was not provided in route!")
        )
    }
}
