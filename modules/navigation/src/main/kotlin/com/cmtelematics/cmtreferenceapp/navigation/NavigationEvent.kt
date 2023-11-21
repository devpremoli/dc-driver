package com.cmtelematics.cmtreferenceapp.navigation

sealed interface NavigationEvent {

    data class BackNavigation(
        val routeFactory: Route.Factory<*>? = null,
        val result: NavigationResult? = null,
        val inclusive: Boolean = false
    ) : NavigationEvent

    data class ScreenNavigation(
        val route: Route,
        val skipIfAlreadyCurrentRoute: Boolean = false,
        val navigationOptions: NavigationOptions? = null
    ) : NavigationEvent
}
