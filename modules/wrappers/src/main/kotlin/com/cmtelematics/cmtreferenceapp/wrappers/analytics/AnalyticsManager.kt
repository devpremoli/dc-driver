package com.cmtelematics.cmtreferenceapp.wrappers.analytics

import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.navigation.Route

/**
 * Provides analytics related logging functionality.
 */
interface AnalyticsManager {

    /**
     * Logs the screen change from the left behind screen [previousScreen] to the
     * currently active screen [currentScreen].
     */
    suspend fun logScreenNavigation(previousScreen: Route?, currentScreen: Route?)

    /**
     * Logs the currently adjusted state [enabled] of alert type [alertType] with id of alert event [eventIdentifier].
     */
    suspend fun logAlertSetting(eventIdentifier: String, alertType: AlertType, enabled: Boolean)

    /**
     * Logs the the id of current alert event [eventIdentifier] with the sound output of the alert
     * if this alert type [alertType] is enabled [enabled].
     */
    suspend fun logAlertPlayback(eventIdentifier: String, alertType: AlertType, enabled: Boolean)
}
