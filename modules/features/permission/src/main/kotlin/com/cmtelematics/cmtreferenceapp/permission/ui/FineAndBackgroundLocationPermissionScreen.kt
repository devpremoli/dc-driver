package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.FineAndBackgroundLocationPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun FineAndBackgroundLocationPermissionScreen(
    viewModel: FineAndBackgroundLocationPermissionViewModel = hiltViewModel()
) {
    FineAndBackgroundLocationPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun FineAndBackgroundLocationPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit
) {
    PermissionScreenContent(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates,
        title = stringResource(id = R.string.location_permission_title),
        description = stringResource(R.string.fine_and_background_location_permission_description)
    )
}

@Preview
@Composable
private fun FineAndBackgroundLocationPermissionScreenContentPreview() {
    AppTheme {
        FineAndBackgroundLocationPermissionScreenContent(
            requiredPermissionType = PermissionType.FineAndBackgroundLocation,
            markAsRequested = { },
            refreshPermissionStates = { }
        )
    }
}
