package com.cmtelematics.cmtreferenceapp.trips.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.common.util.emptyImmutableList
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton
import com.cmtelematics.cmtreferenceapp.trips.model.MapEvent
import com.cmtelematics.cmtreferenceapp.trips.model.MapPath
import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailsViewModel
import com.cmtelematics.cmtreferenceapp.trips.ui.component.DetailInfoHeader
import com.cmtelematics.cmtreferenceapp.trips.ui.component.RoutePreview
import com.cmtelematics.cmtreferenceapp.trips.ui.component.dialog.MarkerInfoDialog
import com.cmtelematics.cmtreferenceapp.trips.ui.component.dialog.TransportationModeSelectorDialog
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyMapScoredTrip
import com.cmtelematics.cmtreferenceapp.trips.util.createDummyTrip
import com.cmtelematics.sdk.types.MapTrip
import com.cmtelematics.sdk.types.ProcessedTripSummary
import com.cmtelematics.sdk.types.UserTransportationMode
import com.cmtelematics.sdk.types.UserTransportationMode.DRIVER
import kotlinx.collections.immutable.toImmutableList
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun TripDetailsScreen(viewModel: TripDetailsViewModel = hiltViewModel()) {
    val trip by viewModel.trip.collectAsStateInLifecycle()
    val tripDetail by viewModel.tripDetail.collectAsStateInLifecycle()
    val tripMapPath by viewModel.tripMapPath.collectAsStateInLifecycle()
    val mapEvents by viewModel.mapEvents.collectAsStateInLifecycle()
    val showTransportationModeSelectorDialog by viewModel.showTransportationModeSelectorDialog
        .collectAsStateInLifecycle()
    val tripTransportationMode by viewModel.tripTransportationMode.collectAsStateInLifecycle()
    val showMapEventInfoDialog by viewModel.showMapEventInfoDialog.collectAsStateInLifecycle()
    val selectedMapEvent by viewModel.selectedMapEvent.collectAsStateInLifecycle()

    TripDetailsScreenContent(
        navigateBack = { viewModel.navigateBack() },
        trip = trip,
        tripDetail = tripDetail,
        tripMapPath = tripMapPath,
        mapEvents = mapEvents,
        transportationMode = tripTransportationMode,
        showTransportationModeSelectorDialog = showTransportationModeSelectorDialog,
        showMapEventInfoDialog = showMapEventInfoDialog,
        selectedMapEvent = selectedMapEvent,
        selectTransportationMode = { viewModel.selectTransportationMode() },
        cancelTransportationModeSelection = { viewModel.cancelTransportationModeSelection() },
        setUserTransportationMode = { viewModel.setUserTransportationMode(it) },
        cancelMapEventInfo = { viewModel.cancelMapEventInfo() },
        showEventInfo = { viewModel.showEventInfo(it) }
    )
}

@Composable
private fun TripDetailsScreenContent(
    navigateBack: () -> Unit,
    trip: ProcessedTripSummary?,
    tripMapPath: List<MapPath>?,
    mapEvents: List<MapEvent>,
    tripDetail: MapTrip?,
    transportationMode: UserTransportationMode?,
    showTransportationModeSelectorDialog: Boolean,
    showMapEventInfoDialog: Boolean,
    selectedMapEvent: MapEvent?,
    selectTransportationMode: () -> Unit,
    cancelTransportationModeSelection: () -> Unit,
    setUserTransportationMode: (UserTransportationMode) -> Unit,
    cancelMapEventInfo: () -> Unit,
    showEventInfo: (MapEvent) -> Unit
) {
    ScreenScaffold(
        backgroundColor = AppTheme.colors.background.content,
        toolbar = {
            trip?.let { trip ->
                DateToolbar(
                    navigateBack = navigateBack,
                    tripDate = trip.start.ts.time,
                    zoneId = ZoneId.of(trip.tz.id)
                )
            }
        }
    ) {
        val detailHeaderMarginTop = AppTheme.dimens.margin.default

        var detailHeaderHeight by remember { mutableStateOf(0) }
        val detailHeaderHeightDp = with(LocalDensity.current) { detailHeaderHeight.toDp() }

        Box {
            tripDetail?.let { tripDetail ->
                RoutePreview(
                    modifier = Modifier.fillMaxSize(),
                    mapPaths = tripMapPath?.toImmutableList() ?: emptyImmutableList(),
                    events = mapEvents.toImmutableList(),
                    mapBoundingBox = tripDetail.boundingBox,
                    showEventInfo = showEventInfo,
                    contentPaddingTop = detailHeaderHeightDp + detailHeaderMarginTop
                )
            }
            trip?.let { trip ->
                Column {
                    Spacer(modifier = Modifier.height(detailHeaderMarginTop))
                    DetailInfoHeader(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimens.margin.default)
                            .onGloballyPositioned { detailHeaderHeight = it.size.height },
                        trip = trip,
                        transportationMode = transportationMode ?: error("transportationMode must not be null"),
                        selectTransportationMode = selectTransportationMode
                    )
                }
            }
        }
    }

    TransportationModeSelectorDialog(
        showDialog = showTransportationModeSelectorDialog,
        cancel = cancelTransportationModeSelection,
        setUserTransportationMode = setUserTransportationMode
    )

    selectedMapEvent?.let { MarkerInfoDialog(showMapEventInfoDialog, it, cancelMapEventInfo) }
}

@Composable
private fun DateToolbar(navigateBack: () -> Unit, tripDate: Long, zoneId: ZoneId) {
    val date = Instant.ofEpochMilli(tripDate).atZone(zoneId)
    val formattedDate = dateFormatter.format(date)

    Toolbar(
        action = {
            ToolbarBackButton(
                onClick = navigateBack
            )
        },
        title = formattedDate
    )
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

@Preview
@Composable
private fun TripDetailsScreenPreview() {
    AppTheme {
        TripDetailsScreenContent(
            navigateBack = { },
            trip = createDummyTrip(),
            tripMapPath = emptyList(),
            mapEvents = emptyList(),
            tripDetail = createDummyMapScoredTrip(),
            transportationMode = DRIVER,
            showTransportationModeSelectorDialog = false,
            showMapEventInfoDialog = false,
            selectTransportationMode = { },
            cancelTransportationModeSelection = { },
            setUserTransportationMode = { },
            selectedMapEvent = null,
            cancelMapEventInfo = { },
            showEventInfo = { }
        )
    }
}

@Preview
@Composable
private fun TripDetailsScreenWithSelectorDialogPreview() {
    AppTheme {
        TripDetailsScreenContent(
            navigateBack = { },
            trip = createDummyTrip(),
            tripMapPath = emptyList(),
            mapEvents = emptyList(),
            tripDetail = createDummyMapScoredTrip(),
            transportationMode = DRIVER,
            showTransportationModeSelectorDialog = true,
            showMapEventInfoDialog = false,
            selectTransportationMode = { },
            cancelTransportationModeSelection = { },
            setUserTransportationMode = { },
            selectedMapEvent = null,
            cancelMapEventInfo = { },
            showEventInfo = { }
        )
    }
}
