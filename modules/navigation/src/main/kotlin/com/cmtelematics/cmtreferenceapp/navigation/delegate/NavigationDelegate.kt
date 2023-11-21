package com.cmtelematics.cmtreferenceapp.navigation.delegate

import android.os.Bundle
import androidx.navigation.NavController
import com.cmtelematics.cmtreferenceapp.navigation.Constants.ARG_RESULT
import com.cmtelematics.cmtreferenceapp.navigation.NavigationEvent
import timber.log.Timber

class NavigationDelegate(private val navController: NavController) {

    fun navigate(navigationEvent: NavigationEvent) {
        Timber.d("Received navigation event: %s", navigationEvent)
        when (navigationEvent) {
            is NavigationEvent.BackNavigation -> {
                val routeFactory = navigationEvent.routeFactory

                if (routeFactory == null) {
                    navController.popBackStack()
                } else {
                    navController.popBackStack(routeFactory.path, inclusive = navigationEvent.inclusive)
                }

                setResult(navigationEvent)
            }
            is NavigationEvent.ScreenNavigation -> {
                val route = navigationEvent.route
                if (
                    navigationEvent.skipIfAlreadyCurrentRoute &&
                    navController.currentBackStackEntry?.run { destination.route } == route.toPath()
                ) {
                    return
                }

                navController.navigate(route.toPath()) {
                    navigationEvent.navigationOptions?.let { options ->
                        popUpTo(options.popUpTo.path) {
                            inclusive = options.popUpToInclusive
                        }
                    }
                }

                setRoute(navigationEvent)
            }
        }
    }

    private fun setResult(navigationEvent: NavigationEvent.BackNavigation) {
        navController.currentBackStackEntry?.run {
            savedStateHandle[ARG_RESULT] = navigationEvent.result
        }
    }

    private fun setRoute(navigationEvent: NavigationEvent.ScreenNavigation) {
        val route = navigationEvent.route
        val entry = navController.currentBackStackEntry
        val bundle = Bundle()
        bundle.putAll(route.toBundle())
        entry?.arguments?.putAll(bundle)
    }
}
