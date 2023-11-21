package com.cmtelematics.cmtreferenceapp.welcome

import androidx.navigation.NavGraphBuilder
import com.cmtelematics.cmtreferenceapp.common.navigation.composable
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.welcome.ui.WelcomeScreen

fun NavGraphBuilder.welcomeNavigation() {
    composable(WelcomeRoute) { WelcomeScreen() }
}
