package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.ui.util.OnLifecycleEvent
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.PreciseAndBackgroundLocationTrapDoorViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionDescription
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionInstructions
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionRequesterScaffoldContainer
import com.cmtelematics.cmtreferenceapp.permission.util.TrapDoorBackHandler
import com.cmtelematics.cmtreferenceapp.permission.util.TrapDoorRequestStartedState
import com.cmtelematics.cmtreferenceapp.permission.util.createOpenSettings
import com.cmtelematics.cmtreferenceapp.permission.util.rememberTrapDoorRequestStartedState
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton

@Composable
internal fun PreciseAndBackgroundLocationTrapDoorScreen(
    viewModel: PreciseAndBackgroundLocationTrapDoorViewModel = hiltViewModel()
) {
    PreciseAndBackgroundLocationTrapDoorScreenContent(
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun PreciseAndBackgroundLocationTrapDoorScreenContent(
    refreshPermissionStates: () -> Unit
) {
    val permissionRequestStarted: TrapDoorRequestStartedState = rememberTrapDoorRequestStartedState()
    val openSettings by rememberUpdatedState(
        createOpenSettings()
    )

    OnLifecycleEvent {
        if (permissionRequestStarted.getAndSet(false)) {
            refreshPermissionStates()
        }
    }

    TrapDoorBackHandler()

    PermissionRequesterScaffoldContainer {
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = stringResource(id = R.string.location_permission_title),
            style = AppTheme.typography.title.xxLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        PermissionDescription(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.margin.default),
            description = stringResource(R.string.precise_and_background_location_trapdoor_description),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

        PermissionInstructions(
            instruction = stringResource(R.string.precise_and_background_location_trapdoor_instruction)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraLarge))

        Image(
            modifier = Modifier
                .fillMaxSize(1f)
                .weight(1f),
            painter = painterResource(
                id = R.drawable.ic_precise_and_background_location_trapdoor
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.trapdoor_open_settings_button),
            onClick = {
                permissionRequestStarted.set(true)

                openSettings()
            },
            buttonSize = ButtonSize.Large
        )
    }
}

@Preview
@Composable
private fun PreciseAndBackgroundLocationTrapDoorScreenContentPreview() {
    AppTheme {
        PreciseAndBackgroundLocationTrapDoorScreenContent(
            refreshPermissionStates = { }
        )
    }
}
