package com.cmtelematics.cmtreferenceapp.tags.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.makeDummyVehicle
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.tags.R
import com.cmtelematics.cmtreferenceapp.tags.model.VehicleDetailsViewModel
import com.cmtelematics.cmtreferenceapp.tags.util.makeDummyTag
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.HeaderRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton
import com.cmtelematics.sdk.types.TagSummary
import com.cmtelematics.sdk.types.Vehicle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun VehicleDetailsScreen(viewModel: VehicleDetailsViewModel = hiltViewModel()) {
    val vehicle by viewModel.vehicle.collectAsStateInLifecycle()
    val tagSummary by viewModel.tagSummary.collectAsStateInLifecycle()
    VehicleDetailsScreenContent(
        navigateBack = { viewModel.navigateBack() },
        vehicle = vehicle,
        tagSummary = tagSummary
    )
}

@Composable
private fun VehicleDetailsScreenContent(
    navigateBack: () -> Unit,
    vehicle: Vehicle?,
    tagSummary: TagSummary?
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                title = vehicle?.nickname ?: stringResource(R.string.loading),
                action = {
                    ToolbarBackButton(onClick = navigateBack)
                }
            )
        },
        backgroundColor = AppTheme.colors.background.content
    ) {
        if (vehicle == null) {
            LoadingContent()
        } else {
            VehicleContent(vehicle, tagSummary)
        }
    }
}

@Composable
fun VehicleContent(
    vehicle: Vehicle,
    tagSummary: TagSummary?
) {
    HeaderRow(title = stringResource(R.string.table_header_vehicle_information))
    TextRow(rowTitle = stringResource(R.string.details_row_name), rowValue = vehicle.nickname)
    TextRow(rowTitle = stringResource(R.string.details_row_make), rowValue = vehicle.make)
    TextRow(rowTitle = stringResource(R.string.details_row_model), rowValue = vehicle.model)

    if (tagSummary != null) {
        TagContent(tagSummary)
    } else {
        HeaderRow(title = stringResource(R.string.table_header_tag_info_loading))
    }
}

@Composable
private fun TagContent(tagSummary: TagSummary) {
    HeaderRow(title = stringResource(R.string.table_header_tag_information))
    TextRow(rowTitle = stringResource(R.string.details_row_tag_mac), rowValue = tagSummary.mac)
    TextRow(
        rowTitle = stringResource(R.string.details_row_tag_status),
        rowValue = if (tagSummary.isConnected) {
            stringResource(R.string.tag_status_connected)
        } else {
            stringResource(R.string.tag_status_not_connected)
        }
    )
    TextRow(
        rowTitle = stringResource(R.string.details_row_tag_last_connected),
        rowValue = dateFormatter.format(
            Instant.ofEpochMilli(tagSummary.lastConnectionTs).atZone(ZoneId.systemDefault())
        )
    )
    TextRow(rowTitle = stringResource(R.string.details_row_tag_versions), rowValue = tagSummary.version)
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.loading))
    }
}

@Preview
@Composable
private fun VehicleDetailsLoadingPreview() {
    AppTheme {
        VehicleDetailsScreenContent(
            navigateBack = {},
            vehicle = null,
            tagSummary = null
        )
    }
}

@Preview
@Composable
private fun VehicleDetailsScreenPreview() {
    AppTheme {
        VehicleDetailsScreenContent(
            navigateBack = {},
            vehicle = makeDummyVehicle("Mini Cooper S", "Mini", "Cooper S"),
            tagSummary = makeDummyTag()
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
