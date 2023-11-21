package com.cmtelematics.cmtreferenceapp.trips.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.trips.TripDetailsRoute
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.Loaded
import com.cmtelematics.cmtreferenceapp.trips.repository.TripDetailRepository
import com.cmtelematics.cmtreferenceapp.trips.util.getMapEvent
import com.cmtelematics.cmtreferenceapp.trips.util.getRouteSegments
import com.cmtelematics.cmtreferenceapp.trips.util.mapToMapPaths
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import com.cmtelematics.sdk.types.MapScoredTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.UserTransportationMode
import com.cmtelematics.sdk.types.UserTransportationMode.DRIVER
import com.cmtelematics.sdk.types.UserTransportationMode.PASSENGER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Trip details screen.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param tripManager Needed to provide the list of trips, and trip refreshing opportunity.
 * @param dispatcherProvider Needed to provide the desired CoroutineDispatcher.
 * @param tripDetailRepository Needed to read and evict the trip detail cache.
 */
@HiltViewModel
internal class TripDetailsViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val tripManager: TripManager,
    private val dispatcherProvider: DispatcherProvider,
    private val tripDetailRepository: TripDetailRepository
) : BaseScreenViewModel(navigator, errorService) {
    private val mutableShowTransportationModeSelectorDialog = MutableStateFlow(false)
    private val mutableTripTransportationMode = MutableStateFlow<UserTransportationMode?>(null)

    private val mutableShowMapEventInfoDialog = MutableStateFlow(false)
    private val mutableSelectedMapEvent = MutableStateFlow<MapEvent?>(null)

    val showTransportationModeSelectorDialog = mutableShowTransportationModeSelectorDialog.asStateFlow()
    val tripTransportationMode = mutableTripTransportationMode.asStateFlow()

    val showMapEventInfoDialog = mutableShowMapEventInfoDialog.asStateFlow()
    val selectedMapEvent = mutableSelectedMapEvent.asStateFlow()

    private val tripFlow: Flow<ProcessedTripSummary> = currentRouteFlow<TripDetailsRoute>()
        .map { it.tripDriveId }
        .flatMapLatest { tripDriveId ->
            tripManager.tripList.map { list ->
                list.first { it.driveId == tripDriveId }
            }
        }

    private val tripDetailStateFlow = tripFlow.flatMapLatest { trip ->
        tripDetailRepository.detailCache.map { it[trip.driveId] }
    }

    private val tripDetailFlow = tripDetailStateFlow.filterNotNull()
        .filterIsInstance<Loaded>()
        .map { it.tripDetail }

    val trip = tripFlow.stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = null)

    val tripDetail = tripDetailFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val tripMapPath: StateFlow<List<MapPath>?> = combine(tripFlow.map { it.tz }, tripDetailFlow, ::Pair)
        .map { (timeZone, tripDetail) ->
            withContext(dispatcherProvider.default) {
                getRouteSegments(
                    tripDetail = tripDetail,
                    timeZone = timeZone
                ).mapToMapPaths()
            }
        }
        .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = null)

    val mapEvents = combine(tripFlow.map { it.tz.id }, tripDetailFlow.filterIsInstance<MapScoredTrip>(), ::Pair)
        .map { (timeZoneId, tripDetail) -> tripDetail.events.map { getMapEvent(it, timeZoneId) } }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = emptyList())

    init {
        viewModelScope.launch {
            mutableTripTransportationMode.value = tripDetailStateFlow.filterNotNull().map { tripDetailState ->
                (tripDetailState as? Loaded)?.transportationMode ?: error("tripDetail must be Loaded")
            }.first()
        }
    }

    fun selectTransportationMode() {
        mutableShowTransportationModeSelectorDialog.value = true
    }

    fun setUserTransportationMode(transportationMode: UserTransportationMode) = viewModelScope.launch {
        val trip = trip.value ?: error("trip must be not null!")
        val currentTransportationMode = tripTransportationMode.value ?: error("trip must be not null!")

        if (
            needToModifyTripTransportationMode(
                currentTransportationMode = currentTransportationMode,
                selectedTransportationMode = transportationMode
            )
        ) {
            tripManager.setUserTransportationMode(trip.driveId, transportationMode)
            tripDetailRepository.evictDetail(trip.driveId)
            mutableTripTransportationMode.value = tripManager.getUserTransportationMode(trip)
        }
        cancelTransportationModeSelection()
    }

    private fun needToModifyTripTransportationMode(
        currentTransportationMode: UserTransportationMode,
        selectedTransportationMode: UserTransportationMode
    ): Boolean {
        val isCurrentTransportationModeIsNotACar =
            currentTransportationMode != PASSENGER && currentTransportationMode != DRIVER

        return if (
            isCurrentTransportationModeIsNotACar &&
            (selectedTransportationMode == PASSENGER || selectedTransportationMode == DRIVER)
        ) {
            true
        } else if (!isCurrentTransportationModeIsNotACar) {
            selectedTransportationMode != currentTransportationMode
        } else {
            false
        }
    }

    fun cancelTransportationModeSelection() {
        mutableShowTransportationModeSelectorDialog.value = false
    }

    fun showEventInfo(event: MapEvent) {
        mutableSelectedMapEvent.value = event
        mutableShowMapEventInfoDialog.value = true
    }

    fun cancelMapEventInfo() {
        mutableShowMapEventInfoDialog.value = false
        mutableSelectedMapEvent.value = null
    }
}
