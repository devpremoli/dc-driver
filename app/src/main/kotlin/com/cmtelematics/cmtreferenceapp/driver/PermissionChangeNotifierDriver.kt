package com.cmtelematics.cmtreferenceapp.driver

interface PermissionChangeNotifierDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
