package com.cmtelematics.cmtreferenceapp.wrappers.trip

import kotlinx.coroutines.flow.StateFlow

/**
 * Provides functionality related to trip recording.
 */
interface RecordingManager {
    /**
     * Whether trip recording is in progress.
     */
    val isRecordingInProgress: StateFlow<Boolean>

    /**
     * Start manual trip recording. Returns after [isRecordingInProgress] has become true.
     */
    suspend fun startMockTripRecording()

    /**
     * Stops manual trip recording. Suspends until all related services are stopped.
     */
    suspend fun stopMockTripRecording()
}
