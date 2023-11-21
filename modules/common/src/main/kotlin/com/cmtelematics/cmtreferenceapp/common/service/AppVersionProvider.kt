package com.cmtelematics.cmtreferenceapp.common.service

/**
 * A provider that can be used to get the app version.
 */
interface AppVersionProvider {
    /**
     * Get the app version from the build config.
     */
    val appVersion: String

    /**
     * Get the app version from the build config with app's build number.
     */
    val appVersionWithBuildNumber: String
}
