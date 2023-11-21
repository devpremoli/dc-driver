package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BackgroundLocationPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.SingleInstructionPermissionScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun BackgroundLocationPermissionScreen(viewModel: BackgroundLocationPermissionViewModel = hiltViewModel()) {
    BackgroundLocationPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun BackgroundLocationPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit
) {
    SingleInstructionPermissionScreenContent(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates,
        title = stringResource(id = R.string.location_permission_title),
        description = stringResource(id = R.string.background_location_permission_description),
        instruction = stringResource(id = R.string.background_location_permission_instruction)
    )
}

@Preview
@Composable
private fun BackgroundLocationPermissionScreenContentPreview() {
    AppTheme {
        BackgroundLocationPermissionScreenContent(
            requiredPermissionType = PermissionType.BackgroundLocation,
            markAsRequested = { },
            refreshPermissionStates = { }
        )
    }
}
