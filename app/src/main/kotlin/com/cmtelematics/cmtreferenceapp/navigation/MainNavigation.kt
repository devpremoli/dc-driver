package com.cmtelematics.cmtreferenceapp.navigation

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.authentication.authenticationNavigation
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.crash.crashNavigation
import com.cmtelematics.cmtreferenceapp.dashboard.dashboardNavigation
import com.cmtelematics.cmtreferenceapp.navigation.route.SplashRoute
import com.cmtelematics.cmtreferenceapp.permission.permissionNavigation
import com.cmtelematics.cmtreferenceapp.settings.settingsNavigation
import com.cmtelematics.cmtreferenceapp.tags.tagsNavigation
import com.cmtelematics.cmtreferenceapp.trips.tripListNavigation
import com.cmtelematics.cmtreferenceapp.ui.splash.SplashScreen
import com.cmtelematics.cmtreferenceapp.welcome.welcomeNavigation

internal fun NavGraphBuilder.mainNavigation() {
    composable(SplashRoute) { SplashScreen() }

    welcomeNavigation()
    authenticationNavigation()
    dashboardNavigation()
    tagsNavigation()
    permissionNavigation()
    settingsNavigation()
    tripListNavigation()
    crashNavigation()
}
