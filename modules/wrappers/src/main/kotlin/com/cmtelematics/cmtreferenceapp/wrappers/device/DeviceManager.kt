package com.cmtelematics.cmtreferenceapp.wrappers.device

import kotlinx.coroutines.flow.Flow

/**
 * Provides functionality related to the current device (phone).
 */
interface DeviceManager {
    /**
     * Gets the device identifier for the this device. This identifier doesn't change while the user is logged in.
     * Always emits the current value to new subscribers.
     */
    val deviceIdentifier: Flow<String?>

    /**
     * Query whether the current device has a gyroscope. Will not change during the runtime of the application.
     */
    val hasGyro: Boolean
}
