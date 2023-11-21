package com.cmtelematics.cmtreferenceapp.wrappers.driver

/**
 * A driver class that will monitor the low battery state and store it's value in a holder
 */
internal interface BatteryIsLowForTripRecordingDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
