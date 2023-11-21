package com.cmtelematics.cmtreferenceapp.permission.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.requireActivity
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.GpsTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun GpsTrapDoorScreen(viewModel: GpsTrapDoorViewModel = hiltViewModel()) {
    val openLocationSettings by rememberUpdatedState(
        createOpenLocationSettings()
    )

    GpsTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() },
        openLocationSettings = openLocationSettings
    )
}

@Composable
private fun createOpenLocationSettings(): (() -> Unit) {
    val activity = LocalContext.current.requireActivity()
    return { activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
}

@Composable
private fun GpsTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit,
    openLocationSettings: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.gps_trapdoor_description),
        instructionStepsDescription = stringResource(R.string.gps_trapdoor_steps_description),
        instructionSteps = listOf(
            stringResource(R.string.gps_trapdoor_instruction_step_1),
            stringResource(R.string.gps_trapdoor_instruction_step_2)
        ),
        openPermissionSpecificSettings = openLocationSettings
    )
}

@Preview
@Composable
private fun GpsTrapDoorScreenContentPreview() {
    AppTheme {
        GpsTrapDoorScreenContent(
            refreshPermissionStates = { },
            openLocationSettings = { }
        )
    }
}
