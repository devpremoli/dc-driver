package com.cmtelematics.cmtreferenceapp.navigation.route

import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.navigation.Route

abstract class BaseOnboardingRoute(
    factory: Factory<*>,
    open val shouldReturnToOriginalScreenOnFinish: Boolean
) : Route(factory) {

    override fun toBundle(): Bundle = super.toBundle().apply {
        putBoolean(ARG_SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW, shouldReturnToOriginalScreenOnFinish)
    }

    companion object {
        @JvmStatic
        protected val ARG_SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW = "SHOULD_NAVIGATE_BACK_ON_FINISH_TO_APP_FLOW"
    }
}
