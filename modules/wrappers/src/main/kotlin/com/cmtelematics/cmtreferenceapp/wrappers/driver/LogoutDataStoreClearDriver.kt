package com.cmtelematics.cmtreferenceapp.wrappers.driver

internal interface LogoutDataStoreClearDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
