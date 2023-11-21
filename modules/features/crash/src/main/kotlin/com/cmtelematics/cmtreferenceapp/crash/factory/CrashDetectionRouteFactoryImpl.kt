package com.cmtelematics.cmtreferenceapp.crash.factory

import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedCountDownRoute
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class CrashDetectionRouteFactoryImpl @Inject constructor(
    private val crashManager: CrashManager
) : CrashRouteFactory {
    override suspend fun create(): Route? = if (crashManager.activeCrash.first() != null) {
        CrashDetectedCountDownRoute()
    } else {
        null
    }
}
