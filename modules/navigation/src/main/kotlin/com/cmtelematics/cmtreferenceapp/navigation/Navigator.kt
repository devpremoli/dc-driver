package com.cmtelematics.cmtreferenceapp.navigation

/**
 * Framework independent navigator. Provides navigation methods between routes.
 */
interface Navigator {
    /**
     * Navigates to the starting route.
     * @param navigationOptions NavigationOptions for popping the stack
     */
    suspend fun navigateToHome(navigationOptions: NavigationOptions? = null)

    /**
     * Navigates forward to the requested route.
     *
     * @param route The route where you want to navigate to
     * @param skipIfAlreadyCurrentRoute Navigation will be cancelled if the desired [route] equals
     * with the current one
     * @param navigationOptions NavigationOptions for popping the stack
     */
    suspend fun navigateTo(
        route: Route,
        skipIfAlreadyCurrentRoute: Boolean = false,
        navigationOptions: NavigationOptions? = null
    )

    /**
     * Navigates back to the previous route.
     *
     * @param result The result to pass back to the previous route
     */
    suspend fun navigateBack(result: NavigationResult? = null)

    /**
     * Navigates back to the requested route.
     *
     * @param routeFactory The factory of the route where you want to navigate back to
     * @param result The result to pass back to the destination screen
     * @param inclusive whether the route specified by [routeFactory] should also be popped off the stack
     */
    suspend fun navigateBackTo(
        routeFactory: Route.Factory<*>,
        result: NavigationResult? = null,
        inclusive: Boolean = false
    )

    /**
     * Navigates back to the starting screen.
     *
     * @param result The result to pass back to the starting screen
     */
    suspend fun navigateBackToHome(result: NavigationResult? = null)
}
