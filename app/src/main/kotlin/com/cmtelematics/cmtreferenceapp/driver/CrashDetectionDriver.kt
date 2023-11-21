package com.cmtelematics.cmtreferenceapp.driver

/**
 * A driver class that will monitor the crash detected state and start the crash detection flow immediately
 * if a crash is observed
 */
interface CrashDetectionDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
