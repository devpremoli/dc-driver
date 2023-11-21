package com.cmtelematics.cmtreferenceapp.navigation

import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val routeManager: RouteManager) : Navigator {

    override suspend fun navigateToHome(navigationOptions: NavigationOptions?) =
        navigateTo(routeManager.getDefaultRoute(), false, navigationOptions)

    override suspend fun navigateTo(
        route: Route,
        skipIfAlreadyCurrentRoute: Boolean,
        navigationOptions: NavigationOptions?
    ) {
        routeManager.setRoute(route, skipIfAlreadyCurrentRoute, navigationOptions)
    }

    override suspend fun navigateBack(result: NavigationResult?) =
        routeManager.goBack(result)

    override suspend fun navigateBackTo(routeFactory: Route.Factory<*>, result: NavigationResult?, inclusive: Boolean) =
        routeManager.goBackTo(routeFactory, result, inclusive)

    override suspend fun navigateBackToHome(result: NavigationResult?) =
        routeManager.goBackTo(routeManager.getDefaultRoute().factory, result)
}
