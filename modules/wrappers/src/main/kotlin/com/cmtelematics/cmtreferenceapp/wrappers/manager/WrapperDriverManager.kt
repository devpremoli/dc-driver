package com.cmtelematics.cmtreferenceapp.wrappers.manager

/**
 * Manages background drivers that continually run for the lifetime of the application.
 */
internal interface WrapperDriverManager {

    /**
     * Launches background drivers.
     */
    fun launchDrivers()
}
