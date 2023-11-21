package com.cmtelematics.cmtreferenceapp.trips.model

import kotlin.time.Duration

internal sealed interface ManualRecordingState {
    object NotRecording : ManualRecordingState
    data class Recording(val length: Duration) : ManualRecordingState
    object DriveDetected : ManualRecordingState
    object StandByModeActive : ManualRecordingState
}
