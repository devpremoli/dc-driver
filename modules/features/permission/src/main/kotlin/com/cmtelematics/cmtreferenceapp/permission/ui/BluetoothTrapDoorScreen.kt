package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BluetoothTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun BluetoothTrapDoorScreen(viewModel: BluetoothTrapDoorViewModel = hiltViewModel()) {
    BluetoothTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun BluetoothTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        description = stringResource(R.string.bluetooth_permission_description)
    )
}

@Preview
@Composable
private fun BluetoothTrapDoorScreenContentPreview() {
    AppTheme {
        BluetoothTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
