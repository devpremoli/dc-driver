package com.cmtelematics.cmtreferenceapp.common.manager

/**
 * Manages background drivers that continually run for the lifetime of the application.
 */
interface DriverManager {

    /**
     * Launches background drivers.
     */
    fun launchDrivers()
}
