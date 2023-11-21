package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.ActivityPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.SingleInstructionPermissionScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun ActivityPermissionScreen(viewModel: ActivityPermissionViewModel = hiltViewModel()) {
    ActivityPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun ActivityPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit
) {
    SingleInstructionPermissionScreenContent(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates,
        title = stringResource(id = R.string.activity_permission_title),
        description = stringResource(R.string.activity_permission_description),
        instruction = stringResource(R.string.activity_permission_instruction_step)
    )
}

@Preview
@Composable
private fun ActivityPermissionScreenContentPreview() {
    AppTheme {
        ActivityPermissionScreenContent(
            requiredPermissionType = PermissionType.PhysicalActivity,
            markAsRequested = { },
            refreshPermissionStates = { }
        )
    }
}
