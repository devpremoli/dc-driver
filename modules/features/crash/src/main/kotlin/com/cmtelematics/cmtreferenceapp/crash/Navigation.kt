package com.cmtelematics.cmtreferenceapp.crash

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.crash.ui.AmbulanceCancelledScreen
import com.cmtelematics.cmtreferenceapp.crash.ui.AmbulanceRequestedScreen
import com.cmtelematics.cmtreferenceapp.crash.ui.CrashAssistScreen
import com.cmtelematics.cmtreferenceapp.crash.ui.CrashDetectedCountDownScreen
import com.cmtelematics.cmtreferenceapp.crash.ui.CrashDetectedNegativeScreen
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceCancelledRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceRequestedRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashAssistRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedCountDownRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedNegativeRoute

fun NavGraphBuilder.crashNavigation() {
    crashAssistNavigation()
    crashDetectionNavigation()
}

private fun NavGraphBuilder.crashAssistNavigation() {
    composable(CrashAssistRoute) { CrashAssistScreen() }
}

private fun NavGraphBuilder.crashDetectionNavigation() {
    composable(CrashDetectedCountDownRoute) { CrashDetectedCountDownScreen() }
    composable(CrashDetectedNegativeRoute) { CrashDetectedNegativeScreen() }
    composable(AmbulanceRequestedRoute) { AmbulanceRequestedScreen() }
    composable(AmbulanceCancelledRoute) { AmbulanceCancelledScreen() }
}
