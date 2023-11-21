package com.cmtelematics.cmtreferenceapp.wrappers.crash

import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash
import java.time.Instant

internal interface CrashUpdateScheduler {
    /**
     * Schedules a background task to update a crash
     * @param crashId the id of the [Crash] being updated
     * @param confirmed true if the crash has been confirmed, otherwise false
     * @param confirmationTime the time at which the user confirmed or disconfirmed the crash
     */
    fun scheduleCrashUpdate(crashId: Long, confirmed: Boolean, confirmationTime: Instant)
}
