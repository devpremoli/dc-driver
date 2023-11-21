package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BackgroundLocationTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun BackgroundLocationTrapDoorScreen(viewModel: BackgroundLocationTrapDoorViewModel = hiltViewModel()) {
    BackgroundLocationTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun BackgroundLocationTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.background_location_trapdoor_description)
    )
}

@Preview
@Composable
private fun BackgroundLocationTrapDoorScreenContentPreview() {
    AppTheme {
        BackgroundLocationTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
