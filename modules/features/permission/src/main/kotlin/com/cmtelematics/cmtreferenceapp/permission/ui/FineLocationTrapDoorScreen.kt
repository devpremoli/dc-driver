package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.FineLocationTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun FineLocationTrapDoorScreen(viewModel: FineLocationTrapDoorViewModel = hiltViewModel()) {
    FineLocationTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun FineLocationTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.fine_location_trapdoor_description)
    )
}

@Preview
@Composable
private fun FineLocationTrapDoorScreenContentPreview() {
    AppTheme {
        FineLocationTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
