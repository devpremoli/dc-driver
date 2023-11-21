package com.cmtelematics.cmtreferenceapp.wrappers.driver

internal interface HardBrakeDetectorDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
