package com.cmtelematics.cmtreferenceapp.permission.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BluetoothDisabledTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.TrapDoorScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun BluetoothDisabledTrapDoorScreen(viewModel: BluetoothDisabledTrapDoorViewModel = hiltViewModel()) {
    BluetoothDisabledTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun BluetoothDisabledTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    val enableBluetooth: () -> Unit by rememberUpdatedState(createBluetoothEnableAction())

    TrapDoorScreenContent(
        refreshPermissionStates = refreshPermissionStates,
        openPermissionSpecificSettings = enableBluetooth,
        description = stringResource(R.string.bluetooth_permission_description)
    )
}

@Composable
private fun createBluetoothEnableAction(): (() -> Unit) =
    if (!LocalInspectionMode.current) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { }
        );
        { launcher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)) }
    } else {
        {}
    }

@Preview
@Composable
private fun BluetoothTrapDoorScreenContentPreview() {
    AppTheme {
        BluetoothDisabledTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
