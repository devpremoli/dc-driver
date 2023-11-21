package com.cmtelematics.cmtreferenceapp.navigation

import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Manages the route stack and history.
 */
interface RouteManager {

    /**
     * Goes back to the previous route with an optional result. Think "back button".
     *
     * @param result The route to pass back to the previous route
     */
    suspend fun goBack(result: NavigationResult? = null)

    /**
     * Goes back to a specific route with an optional result.
     *
     * @param routeFactory The routeFactory where you want to go back to
     * @param result The result to pass back to the destination route
     * @param inclusive whether the route specified by [routeFactory] should also be popped off the stack
     *
     */
    suspend fun goBackTo(routeFactory: Route.Factory<*>, result: NavigationResult? = null, inclusive: Boolean = false)

    /**
     * Goes to the requested route.
     *
     * @param route The route where you want to move to
     * @param skipIfAlreadyCurrentRoute Navigation will be cancelled if the desired [route] equals
     * with the current one
     * @param navigationOptions The navigation options that has to be applied to the screen change
     */
    suspend fun setRoute(
        route: Route,
        skipIfAlreadyCurrentRoute: Boolean,
        navigationOptions: NavigationOptions? = null
    )

    /**
     * Gets the default route.
     *
     * @return the starting route
     */
    fun getDefaultRoute(): Route

    /**
     * The route manager does not perform any navigation. It updates the history and dispatches
     * the events to a consumer. The consumer can do the actual traversal between routes.
     *
     * @return a channel of navigation events.
     */
    fun dispatch(): ReceiveChannel<NavigationEvent>
}
