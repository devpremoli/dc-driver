package com.cmtelematics.cmtreferenceapp.navigation

import com.cmtelematics.cmtreferenceapp.navigation.route.welcome.WelcomeRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteManagerImpl @Inject constructor() : BaseRouteManager() {

    override fun getDefaultRoute(): Route = WelcomeRoute()
}
