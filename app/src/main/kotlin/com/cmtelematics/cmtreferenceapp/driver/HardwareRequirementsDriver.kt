package com.cmtelematics.cmtreferenceapp.driver

/**
 * A driver class that will monitor the authentication state of the user and check if all hardware requirements are
 * met whenever the user logs in.
 */
interface HardwareRequirementsDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()

    /**
     * Dispatched when an App-Only user logs in and the current device doesn't have a gyroscope.
     */
    class NoGyroInAppOnlyModeException : Exception("Gyroscope required for App-Only mode.")
}
