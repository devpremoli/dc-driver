package com.cmtelematics.cmtreferenceapp.driver

import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.MainGraphRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedCountDownRoute
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CrashDetectionDriverImpl @Inject constructor(
    private val navigator: Navigator,
    private val crashManager: CrashManager,
    private val startupManager: StartupManager
) : CrashDetectionDriver {
    override suspend fun run() {
        startupManager.isAppInitialised().first()
        crashManager.activeCrash
            .filter { it != null }
            .collect {
                navigator.navigateTo(
                    route = CrashDetectedCountDownRoute(),
                    navigationOptions = NavigationOptions(popUpTo = MainGraphRoute, popUpToInclusive = true)
                )
            }
    }
}
