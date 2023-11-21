package com.cmtelematics.cmtreferenceapp.navigation.route.permission

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.route.BaseOnboardingRoute
import kotlinx.parcelize.Parcelize

@Parcelize
class FineLocationPermissionRoute(
    override val shouldReturnToOriginalScreenOnFinish: Boolean
) : BaseOnboardingRoute(this, shouldReturnToOriginalScreenOnFinish) {

    companion object : Factory<FineLocationPermissionRoute>() {

        override val path = "fineLocationPermission"

        override fun create(bundle: Bundle?) = FineLocationPermissionRoute(
            shouldReturnToOriginalScreenOnFinish = bundle?.run {
                getBoolean(ARG_SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW)
            } ?: error("shouldReturnToOriginalScreenOnFinish flag was not provided in route!")
        )
    }
}
