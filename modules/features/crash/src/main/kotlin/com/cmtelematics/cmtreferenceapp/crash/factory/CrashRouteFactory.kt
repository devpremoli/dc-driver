package com.cmtelematics.cmtreferenceapp.crash.factory

import com.cmtelematics.cmtreferenceapp.navigation.Route

interface CrashRouteFactory {

    suspend fun create(): Route?
}
