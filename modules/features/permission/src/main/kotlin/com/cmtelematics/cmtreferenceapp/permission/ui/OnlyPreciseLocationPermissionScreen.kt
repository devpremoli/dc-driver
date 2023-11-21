package com.cmtelematics.cmtreferenceapp.permission.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.model.OnlyPreciseLocationPermissionViewModel
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionDescription
import com.cmtelematics.cmtreferenceapp.permission.ui.component.PermissionRequesterScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton

@Composable
internal fun OnlyPreciseLocationPermissionScreen(viewModel: OnlyPreciseLocationPermissionViewModel = hiltViewModel()) {
    OnlyPreciseLocationPermissionScreenContent(
        requiredPermissionType = viewModel.requiredPermissionType,
        markAsRequested = { viewModel.markAsRequested(it) },
        refreshPermissionStates = { viewModel.refreshPermissionStates() }
    )
}

@Composable
private fun OnlyPreciseLocationPermissionScreenContent(
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
            text = stringResource(id = R.string.trapdoor_title),
            style = AppTheme.typography.title.xxLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        PermissionDescription(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.margin.default),
            description = stringResource(R.string.only_precise_location_permission_description),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        Image(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            painter = painterResource(
                id = R.drawable.ic_only_precise_location_permission
            ),
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.permission_tap_to_enable),
            onClick = { requestPermission() },
            buttonSize = ButtonSize.Large
        )
    }
}

@Preview
@Composable
private fun OnlyPreciseLocationPermissionScreenContentPreview() {
    AppTheme {
        OnlyPreciseLocationPermissionScreenContent(
            requiredPermissionType = PermissionType.OnlyPreciseLocation,
            markAsRequested = { },
            refreshPermissionStates = { }
        )
    }
}
