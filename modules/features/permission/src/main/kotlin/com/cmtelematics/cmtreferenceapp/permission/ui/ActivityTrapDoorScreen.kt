package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.ActivityTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun ActivityTrapDoorScreen(viewModel: ActivityTrapDoorViewModel = hiltViewModel()) {
    ActivityTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun ActivityTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.activity_trapdoor_description)
    )
}

@Preview
@Composable
private fun ActivityTrapDoorScreenContentPreview() {
    AppTheme {
        ActivityTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
