package com.cmtelematics.cmtreferenceapp.common.service

import kotlinx.coroutines.CoroutineDispatcher

/**
 * A provider that can be used to get the desired CoroutineDispatcher.
 */
interface DispatcherProvider {
    /**
     * Get the main CoroutineDispatcher (Dispatchers.Main).
     */
    val main: CoroutineDispatcher

    /**
     * Get the default CoroutineDispatcher (Dispatchers.Default).
     */
    val default: CoroutineDispatcher

    /**
     * Get the io CoroutineDispatcher (Dispatchers.IO).
     */
    val io: CoroutineDispatcher
}
