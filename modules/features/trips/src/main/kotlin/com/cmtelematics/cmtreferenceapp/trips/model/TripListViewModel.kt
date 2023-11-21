package com.cmtelematics.cmtreferenceapp.trips.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.behavior.StandbyModeBehavior
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.GoogleMapsApiKey
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.manager.StandbySettingsManager
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.util.getTicker
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripDetailsRoute
import com.cmtelematics.cmtreferenceapp.trips.repository.TripDetailRepository
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.RecordingManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import com.cmtelematics.sdk.types.ProcessedTripSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * ViewModel for the Trip list screen.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param tripManager Needed to provide the list of trips, and trip refreshing opportunity.
 * @param recordingManager Needed to provide the manual recording related functionality.
 * @param preferences Session related DataStore of application.
 * @param clock Needed to provide the current system time.
 * @param googleMapsApiKey Needed to reach Google Maps Static API, and draw preview of trips.
 */
@Suppress("LongParameterList")
@HiltViewModel
internal class TripListViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val tripManager: TripManager,
    settingsManager: SettingsManager,
    private val recordingManager: RecordingManager,
    @SessionDataStore
    private val preferences: DataStore<Preferences>,
    override val clock: Clock,
    @GoogleMapsApiKey
    val googleMapsApiKey: String,
    private val tripDetailRepository: TripDetailRepository
) : BaseScreenViewModel(navigator, errorService), StandbyModeBehavior {
    override val standbySettingsManager: StandbySettingsManager = settingsManager

    private val manualRecordingProgressFlow = recordingManager.isRecordingInProgress.flatMapLatest { inProgress ->
        getManualRecordingStateFlow(inProgress)
    }

    private val isStandByModeActiveFlow = standbySettingsManager.standbyExpirationDate
        .map { it != null }

    val manualRecordingState: StateFlow<ManualRecordingState> = combine(
        manualRecordingProgressFlow,
        isStandByModeActiveFlow
    ) { recordingProgressState, isStandByModeActive ->
        if (isStandByModeActive) {
            ManualRecordingState.StandByModeActive
        } else {
            recordingProgressState
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ManualRecordingState.NotRecording)

    private val mutableIsRefreshing = MutableStateFlow(false)

    val tripList: StateFlow<List<Pair<ProcessedTripSummary, TripDetailState>>> =
        combine(tripManager.tripList, tripDetailRepository.detailCache) { tripList, detailCache ->
            tripList.map { it to getTripDetailInitialState(it, detailCache) }
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    val isRefreshing = mutableIsRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            tripDetailRepository.clear()
            standbySettingsManager.refreshStandbyMode()
            observeStandbyExpirationDateChange()
        }
        refreshList()
    }

    fun refreshList() = launchWithErrorHandling {
        try {
            mutableIsRefreshing.value = true
            tripManager.refreshTripList()
        } finally {
            mutableIsRefreshing.value = false
        }
    }

    fun loadTripDetails(trip: ProcessedTripSummary) = launchWithErrorHandling {
        tripDetailRepository.loadTripDetails(trip)
    }

    fun toggleManualRecording() = launchWithErrorHandling {
        trackProgress {
            if (recordingManager.isRecordingInProgress.value) {
                recordingManager.stopMockTripRecording()
                preferences.edit { it -= manualRecordingStartTimeKey }
            } else {
                preferences.edit { it[manualRecordingStartTimeKey] = clock.millis() }
                recordingManager.startMockTripRecording()
            }
        }
    }

    fun openTripDetails(driveId: String) = launchWithErrorHandling {
        navigator.navigateTo(TripDetailsRoute(driveId))
    }

    private fun getManualRecordingStateFlow(inProgress: Boolean): Flow<ManualRecordingState> = if (inProgress) {
        flow {
            preferences.data.first()[manualRecordingStartTimeKey]?.let { start ->
                emitAll(getRecordingTickerFromStartTime(start))
            } ?: run {
                emit(ManualRecordingState.DriveDetected)
            }
        }
    } else {
        flowOf(ManualRecordingState.NotRecording)
    }

    private fun getRecordingTickerFromStartTime(start: Long) = getTicker()
        .map { ManualRecordingState.Recording((clock.millis() - start).milliseconds) }

    private fun getTripDetailInitialState(trip: ProcessedTripSummary, detailCache: Map<String, TripDetailState>) =
        detailCache[trip.driveId] ?: if (trip.tagOnly) {
            TripDetailState.NotAvailable
        } else {
            TripDetailState.NotLoaded
        }

    companion object {
        private val manualRecordingStartTimeKey = longPreferencesKey("MANUAL_RECORDING_START")
    }
}
