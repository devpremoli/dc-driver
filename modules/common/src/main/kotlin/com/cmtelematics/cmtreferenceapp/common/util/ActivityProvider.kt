package com.cmtelematics.cmtreferenceapp.common.util

import android.app.Activity
import android.app.Application

/**
 * Provides and stores an instance of [Activity].
 */
interface ActivityProvider {

    /**
     * Initialization method of provider.
     */
    fun initialize(application: Application)

    /**
     * Provider method of the [Activity] instance. Usually returns immediately. May suspend while the activity is
     * initially created during app startup or while the activity is being recreated.
     */
    suspend fun awaitActivity(): Activity
}
