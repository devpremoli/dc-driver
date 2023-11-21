package com.cmtelematics.cmtreferenceapp.wrappers.crash

import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface CrashManager {
    /**
     * The currently active crash [Crash].
     */
    val activeCrash: Flow<Crash?>

    /**
     * The date of the current crash's first notification.
     */
    val firstNotificationTime: Flow<Instant?>

    /**
     * Simulates a crash occurrence, with a fake crash.
     */
    suspend fun simulateCrash()

    /**
     * Set the state of crash flow to started.
     */
    suspend fun notifyFlowStarted()

    /**
     * Handles the selection of the user in the crash flow.
     *
     * @param crashConfirmed is the crash confirmed by the user.
     * @param shouldSendAmbulance is the the ambulance call selected by the user.
     */
    suspend fun handleUserSelection(crashConfirmed: Boolean, shouldSendAmbulance: Boolean)
}
