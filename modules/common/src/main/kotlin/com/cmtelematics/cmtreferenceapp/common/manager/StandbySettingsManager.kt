package com.cmtelematics.cmtreferenceapp.common.manager

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface StandbySettingsManager {
    val standbyExpirationDate: Flow<Instant?>

    suspend fun refreshStandbyMode()

    suspend fun setStandByModeDuration(standByTimeInMinutes: Int)
}
