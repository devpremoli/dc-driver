package com.cmtelematics.cmtreferenceapp.common.factory

import com.cmtelematics.cmtreferenceapp.navigation.Route

/**
 * Determines which permission or trapdoor screen needs to be shown in permission flow.
 */
interface PermissionRouteFactory {

    suspend fun create(shouldReturnToOriginalScreenOnFinish: Boolean): Route?
}
