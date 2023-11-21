package com.cmtelematics.cmtreferenceapp.trips.ui

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.createMarkerBitmap
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.cmtreferenceapp.trips.model.ManualRecordingState
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState.NotLoaded
import com.cmtelematics.cmtreferenceapp.trips.model.TripListViewModel
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.MARKER_NAME_END
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.MARKER_NAME_START
import com.cmtelematics.cmtreferenceapp.trips.ui.Constants.POLYLINE_WIDTH
import com.cmtelematics.cmtreferenceapp.trips.ui.component.HiddenMap
import com.cmtelematics.cmtreferenceapp.trips.ui.component.RecordingButton
import com.cmtelematics.cmtreferenceapp.trips.ui.component.TripItem
import com.cmtelematics.cmtreferenceapp.trips.ui.component.rememberMapViewWithLifecycle
import com.cmtelematics.cmtreferenceapp.trips.util.PolylineOptions
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyTripList
import com.cmtelematics.cmtreferenceapp.trips.util.renderMap
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.TripState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun TripListScreen(viewModel: TripListViewModel = hiltViewModel()) {
    val tripList by viewModel.tripList.collectAsStateInLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateInLifecycle()
    val manualRecordingState by viewModel.manualRecordingState.collectAsStateInLifecycle()

    TripListScreenContent(
        tripList = tripList,
        isRefreshing = isRefreshing,
        refresh = { viewModel.refreshList() },
        toggleManualRecording = { viewModel.toggleManualRecording() },
        manualRecordingState = manualRecordingState,
        loadTripDetails = { viewModel.loadTripDetails(it) },
        openTripDetails = viewModel::openTripDetails
    )
}

@Composable
private fun TripListScreenContent(
    tripList: List<Pair<ProcessedTripSummary, TripDetailState>>,
    isRefreshing: Boolean,
    refresh: () -> Unit,
    toggleManualRecording: () -> Unit,
    manualRecordingState: ManualRecordingState,
    loadTripDetails: (ProcessedTripSummary) -> Unit,
    openTripDetails: (String) -> Unit
) {
    ScreenScaffold(
        backgroundColor = AppTheme.colors.background.content,
        toolbar = { Toolbar(title = stringResource(R.string.title_trip_list)) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val mapView = getMapView()

            mapView?.let { HiddenMap(it) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimens.margin.default,
                        end = AppTheme.dimens.margin.default,
                        top = AppTheme.dimens.margin.default
                    )
            ) {
                RecordingButton(
                    text = getManualRecordingButtonText(manualRecordingState),
                    onClick = toggleManualRecording,
                    modifier = Modifier.fillMaxWidth(),
                    isRecording = manualRecordingState is ManualRecordingState.Recording,
                    enabled = manualRecordingState !in listOf(
                        ManualRecordingState.DriveDetected,
                        ManualRecordingState.StandByModeActive
                    )
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

                val polylineWidth = with(LocalDensity.current) { POLYLINE_WIDTH.toPx() }
                val renderMutex = remember { Mutex() }

                TripList(
                    trips = tripList,
                    isRefreshing = isRefreshing,
                    refresh = refresh,
                    loadTripDetails = loadTripDetails,
                    openTripDetails = openTripDetails,
                    staticMapRenderer = { path, bounds, startPositionIcon, endPositionIcon ->
                        renderMutex.withLock {
                            val googleMap = getRendererGoogleMap(mapView ?: error("mapView must be not null"))
                            renderMap(
                                map = googleMap,
                                polylineOptions = PolylineOptions(
                                    polylineColor = Color.Black,
                                    polylineWidth = polylineWidth,
                                    path = path
                                ),
                                bounds = bounds,
                                startPositionIcon = startPositionIcon,
                                endPositionIcon = endPositionIcon
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun getMapView() = if (!LocalInspectionMode.current) {
    rememberMapViewWithLifecycle()
} else {
    null
}

@Composable
private fun getPositionBitmap(markerName: String, @DrawableRes iconRes: Int) = createMarkerBitmap(markerName) {
    Image(
        modifier = Modifier.scale(MARKER_SCALING),
        painter = painterResource(id = iconRes),
        contentDescription = null
    )
}

private suspend fun getRendererGoogleMap(mapView: MapView): GoogleMap {
    val map = mapView.awaitMap()
    map.uiSettings.apply {
        isCompassEnabled = false
        isTiltGesturesEnabled = false
    }
    return map
}

@Composable
private fun getManualRecordingButtonText(manualRecordingState: ManualRecordingState) = when (manualRecordingState) {
    ManualRecordingState.NotRecording -> stringResource(R.string.button_manual_trip_recording_start)
    is ManualRecordingState.Recording -> manualRecordingState.length.toComponents { hours, minutes, seconds, _ ->
        stringResource(R.string.button_manual_record_in_progress, hours, minutes, seconds)
    }
    ManualRecordingState.DriveDetected -> stringResource(R.string.button_automatic_trip_recording_in_progress)
    ManualRecordingState.StandByModeActive -> stringResource(R.string.button_standby_mode_enabled)
}

@Composable
private fun ColumnScope.TripList(
    trips: List<Pair<ProcessedTripSummary, TripDetailState>>,
    isRefreshing: Boolean,
    refresh: () -> Unit,
    loadTripDetails: (ProcessedTripSummary) -> Unit,
    openTripDetails: (String) -> Unit,
    staticMapRenderer: StaticMapRenderer
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = refresh,
        modifier = Modifier.weight(1f),
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                backgroundColor = AppTheme.colors.background.surface,
                contentColor = AppTheme.colors.primary
            )
        }
    ) {
        Trips(
            trips = trips,
            loadTripDetails = loadTripDetails,
            openTripDetails = openTripDetails,
            staticMapRenderer = staticMapRenderer
        )
    }
}

@Composable
private fun Trips(
    trips: List<Pair<ProcessedTripSummary, TripDetailState>>,
    loadTripDetails: (ProcessedTripSummary) -> Unit,
    openTripDetails: (String) -> Unit,
    staticMapRenderer: StaticMapRenderer
) {
    var startPositionIcon by remember { mutableStateOf<Bitmap?>(null) }
    var endPositionIcon by remember { mutableStateOf<Bitmap?>(null) }

    if (startPositionIcon == null) {
        startPositionIcon = getPositionBitmap(MARKER_NAME_START, R.drawable.ic_start_marker)
    }

    if (endPositionIcon == null) {
        endPositionIcon = getPositionBitmap(MARKER_NAME_END, R.drawable.ic_end_marker)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.margin.default),
        content = {
            items(trips) { (trip, tripDetailState) ->
                TripItem(
                    modifier = if (trip.tripState == TripState.COMPLETE && tripDetailState is TripDetailState.Loaded) {
                        Modifier.clickable { openTripDetails(trip.driveId) }
                    } else {
                        Modifier
                    },
                    trip = trip,
                    tripDetailState = tripDetailState,
                    startPositionIcon = startPositionIcon,
                    endPositionIcon = endPositionIcon,
                    staticMapRenderer = staticMapRenderer
                )
                LaunchedEffect(tripDetailState) {
                    if (tripDetailState is NotLoaded) {
                        loadTripDetails(trip)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))
            }
        }
    )
}

private const val MARKER_SCALING = 0.7f

@Preview
@Composable
private fun TripListScreenContentPreview() {
    AppTheme {
        TripListScreenContent(
            tripList = createDummyTripList(),
            isRefreshing = false,
            refresh = { },
            toggleManualRecording = { },
            manualRecordingState = ManualRecordingState.Recording(3785.seconds),
            loadTripDetails = { },
            openTripDetails = { }
        )
    }
}

@Preview
@Composable
private fun TripListScreenAutoRecordingInProgressContentPreview() {
    AppTheme {
        TripListScreenContent(
            tripList = createDummyTripList(),
            isRefreshing = false,
            refresh = { },
            toggleManualRecording = { },
            manualRecordingState = ManualRecordingState.DriveDetected,
            loadTripDetails = { },
            openTripDetails = { }
        )
    }
}

@Preview
@Composable
private fun TripListScreenStandbyModeActiveContentPreview() {
    AppTheme {
        TripListScreenContent(
            tripList = createDummyTripList(),
            isRefreshing = false,
            refresh = { },
            toggleManualRecording = { },
            manualRecordingState = ManualRecordingState.StandByModeActive,
            loadTripDetails = { },
            openTripDetails = { }
        )
    }
}
