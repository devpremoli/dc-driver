package com.cmtelematics.cmtreferenceapp.permission.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.OnLifecycleEvent
import com.cmtelematics.cmtreferenceapp.common.ui.util.requireActivity
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.BatteryOptimizationPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionDescription
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionRequesterScaffoldContainer
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import java.util.concurrent.atomic.AtomicBoolean

@Composable
internal fun BatteryOptimizationPermissionScreen(viewModel: BatteryOptimizationPermissionViewModel = hiltViewModel()) {
    val permissionRequestStarted = remember { AtomicBoolean(false) }
    OnLifecycleEvent {
        if (permissionRequestStarted.getAndSet(false)) {
            viewModel.refreshPermissionStates()
        }
    }

    val activity = LocalContext.current.requireActivity()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { viewModel.refreshPermissionStates() }
    )

    BatteryOptimizationPermissionScreenContent(
        requestPermission = {
            permissionRequestStarted.set(true)

            viewModel.markAsRequested(viewModel.requiredPermissionType)

            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:${activity.packageName}")
            )

            launcher.launch(intent)
        }
    )
}

@Composable
private fun BatteryOptimizationPermissionScreenContent(requestPermission: () -> Unit) {
    PermissionRequesterScaffoldContainer {
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = stringResource(id = R.string.battery_optimization_permission_title),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title.xxLarge
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.margin.default)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            PermissionDescription(
                description = stringResource(R.string.battery_optimization_permission_description),
                textAlign = TextAlign.Center
            )
        }

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
private fun BatteryOptimizationPermissionScreenContentPreview() {
    AppTheme {
        BatteryOptimizationPermissionScreenContent(
            requestPermission = { }
        )
    }
}
