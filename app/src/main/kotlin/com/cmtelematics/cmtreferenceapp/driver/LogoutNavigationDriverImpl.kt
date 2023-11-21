package com.cmtelematics.cmtreferenceapp.driver

import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.MainGraphRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.util.getUserLoggedOutFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutNavigationDriverImpl @Inject constructor(
    private val navigator: Navigator,
    private val authenticationManager: AuthenticationManager,
    private val startupManager: StartupManager
) : LogoutNavigationDriver {
    override suspend fun run() {
        getUserLoggedOutFlow(startupManager, authenticationManager)
            .onEach {
                // Navigate back to Welcome Screen
                navigator.navigateToHome(NavigationOptions(popUpTo = MainGraphRoute, popUpToInclusive = true))
            }
            .collect()
    }
}
