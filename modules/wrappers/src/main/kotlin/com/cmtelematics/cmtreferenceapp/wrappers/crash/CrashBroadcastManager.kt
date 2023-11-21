package com.cmtelematics.cmtreferenceapp.wrappers.crash

import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash

internal interface CrashBroadcastManager {

    /**
     * This is the receiver that will take the raw crash received from the DriveWell SDK and process it.
     *
     * Evaluates the incoming crash, and stores or update the current one if necessary
     * @param crash the crash data [Crash] of the occurred crash
     */
    suspend fun handleCrashFromBroadcast(crash: Crash)
}
