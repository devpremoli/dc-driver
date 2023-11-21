package com.cmtelematics.cmtreferenceapp.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.dashboard.R
import com.cmtelematics.cmtreferenceapp.dashboard.model.HomeViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar

@Composable
internal fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val isTagUser by viewModel.isTagUser.collectAsStateInLifecycle(initial = false)
    val isBatteryLowForTripRecording by viewModel.isBatteryLowForTripRecording.collectAsStateInLifecycle()

    HomeScreenContent(
        openCrashFeature = { viewModel.openCrashFeature() },
        openVehicles = { viewModel.openVehicles() },
        isTagUser = isTagUser,
        isBatteryLowForTripRecording = isBatteryLowForTripRecording
    )
}

@Composable
private fun HomeScreenContent(
    openCrashFeature: () -> Unit,
    openVehicles: () -> Unit,
    isTagUser: Boolean,
    isBatteryLowForTripRecording: Boolean
) {
    ScreenScaffold(
        toolbar = { Toolbar(title = stringResource(R.string.title_home)) },
        backgroundColor = AppTheme.colors.background.content,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(color = AppTheme.colors.divider)
        if (isBatteryLowForTripRecording) {
            TripDetectionSuppressionBanner(modifier = Modifier.fillMaxWidth())
        }
        TextRow(
            rowTitle = stringResource(R.string.button_crash_feature),
            modifier = Modifier.clickable(onClick = openCrashFeature)
        )
        if (isTagUser) {
            TextRow(
                rowTitle = stringResource(R.string.button_vehicle_list),
                modifier = Modifier.clickable(onClick = openVehicles)
            )
        }
    }
}

@Composable
private fun TripDetectionSuppressionBanner(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(AppTheme.colors.state.danger)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimens.margin.default,
                    vertical = AppTheme.dimens.margin.small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_banner_warning),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(AppTheme.dimens.margin.default))

            Text(
                text = stringResource(id = R.string.trip_detection_suppression_banner_description),
                style = AppTheme.typography.text.medium,
                color = AppTheme.colors.background.primary
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(
            openCrashFeature = {},
            openVehicles = {},
            isTagUser = true,
            isBatteryLowForTripRecording = true
        )
    }
}

@Preview
@Composable
private fun HomeScreenAppOnlyPreview() {
    AppTheme {
        HomeScreenContent(
            openCrashFeature = {},
            openVehicles = {},
            isTagUser = false,
            isBatteryLowForTripRecording = true
        )
    }
}
