package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BluetoothPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionRequesterScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.EmphasizedText

@Composable
internal fun BluetoothPermissionScreen(viewModel: BluetoothPermissionViewModel = hiltViewModel()) {
    BluetoothPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun BluetoothPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit
) {
    PermissionRequesterScreenScaffold(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates
    ) { requestPermission ->

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = stringResource(id = R.string.bluetooth_permission_title),
            style = AppTheme.typography.title.xxLarge
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        EmphasizedText(
            text = stringResource(R.string.bluetooth_permission_description),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.permission_next_button),
            onClick = { requestPermission() },
            buttonSize = ButtonSize.Large
        )
    }
}

@Preview
@Composable
private fun BluetoothPermissionScreenContentPreview() {
    AppTheme {
        BluetoothPermissionScreenContent(
            requiredPermissionType = PermissionType.Bluetooth,
            markAsRequested = { },
            refreshPermissionStates = { }
        )
    }
}
