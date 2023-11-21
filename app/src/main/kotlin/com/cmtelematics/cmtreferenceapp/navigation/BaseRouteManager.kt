package com.cmtelematics.cmtreferenceapp.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber

abstract class BaseRouteManager : RouteManager {
    private val navigationEvent = Channel<NavigationEvent>(
        capacity = CHANNEL_DEFAULT_CAPACITY,
        onUndeliveredElement = { Timber.e("Failed to deliver navigation event: %s", it) }
    )

    override suspend fun goBack(result: NavigationResult?) {
        navigationEvent.send(NavigationEvent.BackNavigation(result = result))
    }

    override suspend fun goBackTo(routeFactory: Route.Factory<*>, result: NavigationResult?, inclusive: Boolean) {
        navigationEvent.send(NavigationEvent.BackNavigation(routeFactory, result, inclusive))
    }

    override suspend fun setRoute(
        route: Route,
        skipIfAlreadyCurrentRoute: Boolean,
        navigationOptions: NavigationOptions?
    ) {
        Timber.d("Setting route: %s", route)
        navigationEvent.send(
            NavigationEvent.ScreenNavigation(
                route = route,
                skipIfAlreadyCurrentRoute = skipIfAlreadyCurrentRoute,
                navigationOptions = navigationOptions
            )
        )
    }

    override fun dispatch(): ReceiveChannel<NavigationEvent> = navigationEvent

    companion object {
        private const val CHANNEL_DEFAULT_CAPACITY = 64 // kotlinx.coroutines.channels.Channel.CHANNEL_DEFAULT_CAPACITY
    }
}
