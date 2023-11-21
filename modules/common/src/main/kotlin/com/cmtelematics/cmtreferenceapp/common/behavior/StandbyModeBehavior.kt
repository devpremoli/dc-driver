package com.cmtelematics.cmtreferenceapp.common.behavior

import com.cmtelematics.cmtreferenceapp.common.manager.StandbySettingsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.Instant

interface StandbyModeBehavior {
    val clock: Clock

    val standbySettingsManager: StandbySettingsManager

    suspend fun observeStandbyExpirationDateChange() {
        standbySettingsManager.standbyExpirationDate.mapToRequiredDelayUntilStandbyModeExpiration()
            .filterNotNull()
            .collectLatest { requiredDelayUntilStandbyModeExpiration ->
                delay(requiredDelayUntilStandbyModeExpiration)
                standbySettingsManager.refreshStandbyMode()
            }
    }

    private fun Flow<Instant?>.mapToRequiredDelayUntilStandbyModeExpiration() =
        map { standbyExpirationDate ->
            if (standbyExpirationDate?.isAfter(clock.instant()) == true) {
                standbyExpirationDate.minusMillis(clock.millis()).toEpochMilli()
            } else {
                null
            }
        }
}
