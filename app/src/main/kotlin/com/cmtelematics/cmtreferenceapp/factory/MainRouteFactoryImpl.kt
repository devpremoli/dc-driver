package com.cmtelematics.cmtreferenceapp.factory

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.factory.PermissionRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.crash.factory.CrashRouteFactory
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.DashboardRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedOut
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRouteFactoryImpl @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val crashRouteFactory: CrashRouteFactory,
    private val permissionRouteFactory: PermissionRouteFactory,
    private val dispatcherProvider: DispatcherProvider
) : MainRouteFactory {

    override suspend fun createOrDefault(): Route =
        create(shouldReturnToOriginalScreenOnFinish = false) ?: DashboardRoute()

    override suspend fun create(shouldReturnToOriginalScreenOnFinish: Boolean): Route? =
        when (getAuthenticationState()) {
            is LoggedOut -> WelcomeRoute()
            is LoggedIn -> crashRouteFactory.create() ?: permissionRouteFactory.create(
                shouldReturnToOriginalScreenOnFinish = shouldReturnToOriginalScreenOnFinish
            )
        }

    private suspend fun getAuthenticationState() = withContext(dispatcherProvider.default) {
        authenticationManager.state.first()
    }
}
