package com.cmtelematics.cmtreferenceapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.cmtelematics.cmtreferenceapp.navigation.Route
import androidx.navigation.compose.NavHost as AndroidXNavHost

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Route.Factory<*>,
    routeFactory: Route.Factory<*>? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    AndroidXNavHost(
        navController = navController,
        startDestination = startDestination.path,
        route = routeFactory?.path,
        modifier = modifier,
        builder = builder
    )
}
