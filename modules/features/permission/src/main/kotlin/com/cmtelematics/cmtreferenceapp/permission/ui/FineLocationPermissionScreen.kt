package com.cmtelematics.cmtreferenceapp.permission.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.FineLocationPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionDescription
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionInstructions
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionRequesterScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton

@Composable
internal fun FineLocationPermissionScreen(viewModel: FineLocationPermissionViewModel = hiltViewModel()) {
    FineLocationPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() },
        hasPermissionInstruction = viewModel.hasPermissionInstruction
    )
}

@Composable
private fun FineLocationPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit,
    hasPermissionInstruction: Boolean
) {
    PermissionRequesterScreenScaffold(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates
    ) { requestPermission ->

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = stringResource(id = R.string.location_permission_title),
            style = AppTheme.typography.title.xxLarge
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.margin.default)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PermissionDescription(
                description = stringResource(R.string.fine_location_permission_description),
                textAlign = TextAlign.Center
            )

            if (hasPermissionInstruction) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

                PermissionInstructions(stringResource(R.string.fine_location_permission_instruction))
            }
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

@Preview(apiLevel = Build.VERSION_CODES.R)
@Composable
private fun FineLocationPermissionAndroid11ScreenContentPreview() {
    AppTheme {
        FineLocationPermissionScreenContent(
            requiredPermissionType = PermissionType.FineLocation,
            markAsRequested = { },
            refreshPermissionStates = { },
            hasPermissionInstruction = true
        )
    }
}

@Preview(apiLevel = Build.VERSION_CODES.P)
@Composable
private fun FineLocationPermissionAndroid9ScreenContentPreview() {
    AppTheme {
        FineLocationPermissionScreenContent(
            requiredPermissionType = PermissionType.FineLocation,
            markAsRequested = { },
            refreshPermissionStates = { },
            hasPermissionInstruction = false
        )
    }
}
