package com.cmtelematics.cmtreferenceapp.wrappers.driver

/**
 * A driver class that will monitor the authentication state of the user and keep the Firebase Id int the SDK
 * up-to-date whenever the user is logged in.
 */
internal interface FirebaseIdDriver {
    /**
     * Run the driver. This method never returns.
     */
    suspend fun run()
}
