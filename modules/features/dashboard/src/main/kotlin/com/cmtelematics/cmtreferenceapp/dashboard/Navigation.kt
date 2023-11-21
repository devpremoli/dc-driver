package com.cmtelematics.cmtreferenceapp.dashboard

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.dashboard.ui.DashboardScreen
import com.cmtelematics.cmtreferenceapp.dashboard.ui.HomeScreen
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.DashboardRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.HomeRoute
import com.cmtelematics.cmtreferenceapp.settings.settingsTab
import com.cmtelematics.cmtreferenceapp.trips.tripsTab

fun NavGraphBuilder.dashboardNavigation() {
    composable(DashboardRoute) { DashboardScreen() }
}

internal fun NavGraphBuilder.homeTab() {
    composable(HomeRoute) { HomeScreen() }
}

internal fun NavGraphBuilder.dashboardTabsNavigation() {
    homeTab()
    settingsTab()
    tripsTab()
}
