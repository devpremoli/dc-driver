package com.cmtelematics.cmtreferenceapp.settings

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.DebugMenuRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.SettingsRoute
import com.cmtelematics.cmtreferenceapp.settings.ui.DebugMenuScreen
import com.cmtelematics.cmtreferenceapp.settings.ui.SettingsScreen

fun NavGraphBuilder.settingsTab() {
    composable(SettingsRoute) { SettingsScreen() }
}

fun NavGraphBuilder.settingsNavigation() {
    composable(DebugMenuRoute) { DebugMenuScreen() }
}
