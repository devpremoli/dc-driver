package com.cmtelematics.cmtreferenceapp.wrappers.crash

import java.time.Instant

/**
 * Service that handles crash updates on the backend.
 */
internal interface CrashService {
    /**
     * Update the crash on the backend with the supplied data.
     */
    suspend fun updateCrash(crashId: Long, verified: Boolean, verifiedDate: Instant?)
}
