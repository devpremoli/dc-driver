package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.FineAndBackgroundLocationTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun FineAndBackgroundLocationTrapDoorScreen(
    viewModel: FineAndBackgroundLocationTrapDoorViewModel = hiltViewModel()
) {
    FineAndBackgroundLocationTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun FineAndBackgroundLocationTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.fine_and_background_location_trapdoor_description)
    )
}

@Preview
@Composable
private fun FineAndBackgroundLocationTrapDoorScreenContentPreview() {
    AppTheme {
        FineAndBackgroundLocationTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
