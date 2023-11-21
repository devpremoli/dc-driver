package com.cmtelematics.cmtreferenceapp.common.factory

import com.cmtelematics.cmtreferenceapp.navigation.Route

/**
 * Determines the screen to show upon app launch considering different states such as login state and onboarding
 * completion.
 */
interface MainRouteFactory {

    /**
     * Gets the startup route.
     */
    suspend fun createOrDefault(): Route

    suspend fun create(shouldReturnToOriginalScreenOnFinish: Boolean): Route?
}
