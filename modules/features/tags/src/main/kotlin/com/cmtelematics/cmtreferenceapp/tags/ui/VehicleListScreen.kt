package com.cmtelematics.cmtreferenceapp.tags.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.OnLifecycleEvent
import com.cmtelematics.cmtreferenceapp.common.ui.util.makeDummyVehicle
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.tags.R
import com.cmtelematics.cmtreferenceapp.tags.model.VehicleListViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.SecondaryTextRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextButtonRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton
import com.cmtelematics.sdk.types.Vehicle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
internal fun VehicleListScreen(viewModel: VehicleListViewModel = hiltViewModel()) {
    val vehicles by viewModel.vehicles.collectAsStateInLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateInLifecycle()
    VehicleListScreenContent(
        navigateBack = { viewModel.navigateBack() },
        vehicles = vehicles,
        linkTag = viewModel::linkTagToVehicle,
        addVehicle = { viewModel.addVehicle() },
        isRefreshing = isRefreshing,
        refresh = { viewModel.refreshList() },
        openVehicleDetails = viewModel::openVehicleDetails
    )
}

@Composable
private fun VehicleListScreenContent(
    navigateBack: () -> Unit,
    vehicles: List<Vehicle>,
    linkTag: (Vehicle) -> Unit,
    addVehicle: () -> Unit,
    isRefreshing: Boolean,
    refresh: () -> Unit,
    openVehicleDetails: (Vehicle) -> Unit
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                title = stringResource(R.string.title_toolbar_vehicle_list),
                action = {
                    ToolbarBackButton(onClick = navigateBack)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.margin.extraLarge)
            ) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraLarge))
                Text(text = stringResource(R.string.title_vehicle_list), style = AppTheme.typography.title.xxLarge)

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))
                Text(
                    text = stringResource(R.string.subtitle_vehicle_list),
                    style = AppTheme.typography.text.large,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))

            VehicleList(vehicles, isRefreshing, refresh, linkTag, openVehicleDetails)

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))
            TextButton(
                text = stringResource(
                    if (vehicles.isEmpty()) {
                        R.string.button_add_vehicle_empty_list
                    } else {
                        R.string.button_add_vehicle
                    }
                ),
                onClick = addVehicle,
                buttonSize = ButtonSize.Large
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))

            OnLifecycleEvent {
                refresh()
            }
        }
    }
}

@Composable
private fun ColumnScope.VehicleList(
    vehicles: List<Vehicle>,
    isRefreshing: Boolean,
    refresh: () -> Unit,
    linkTag: (Vehicle) -> Unit,
    openVehicleDetails: (Vehicle) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = refresh,
        modifier = Modifier.Companion.weight(1f),
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                backgroundColor = AppTheme.colors.background.surface,
                contentColor = AppTheme.colors.primary
            )
        }
    ) {
        if (vehicles.isNotEmpty()) {
            Vehicles(vehicles = vehicles, linkTag = linkTag, openVehicleDetails = openVehicleDetails)
        } else {
            SecondaryTextRow(title = "You have no vehicles")
            Spacer(modifier = Modifier.Companion.weight(1f))
        }
    }
}

@Composable
private fun Vehicles(
    vehicles: List<Vehicle>,
    linkTag: (Vehicle) -> Unit,
    openVehicleDetails: (Vehicle) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.margin.roomy),
        content = {
            items(vehicles) { vehicle ->
                if (vehicle.tagIsActive) {
                    TextRow(
                        modifier = Modifier.clickable { openVehicleDetails(vehicle) },
                        rowTitle = vehicle.nickname
                    )
                } else {
                    TextButtonRow(
                        rowTitle = vehicle.nickname,
                        buttonTitle = stringResource(R.string.button_link_tag),
                        onClick = { linkTag(vehicle) },
                        modifier = Modifier.clickable { openVehicleDetails(vehicle) }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun EmptyVehicleListScreenPreview() {
    AppTheme {
        VehicleListScreenContent(
            navigateBack = {},
            vehicles = emptyList(),
            linkTag = {},
            addVehicle = {},
            isRefreshing = false,
            refresh = {},
            openVehicleDetails = {}
        )
    }
}

@Preview
@Composable
private fun VehicleListScreenPreview() {
    AppTheme {
        VehicleListScreenContent(
            navigateBack = {},
            vehicles = listOf(
                makeDummyVehicle("Mini Cooper S"),
                makeDummyVehicle("VW Golf", tagLinked = true)
            ),
            linkTag = {},
            addVehicle = {},
            isRefreshing = false,
            refresh = {},
            openVehicleDetails = {}
        )
    }
}
