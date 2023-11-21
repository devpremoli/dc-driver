package com.cmtelematics.cmtreferenceapp.crash.ui

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.model.CrashAssistViewModel
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashAssistMap
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.SwitchRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun CrashAssistScreen(viewModel: CrashAssistViewModel = hiltViewModel()) {
    val isCrashAssistEnabled by viewModel.isCrashAssistEnabled.collectAsStateInLifecycle(initial = true)
    val currentLocation by viewModel.currentLocation.collectAsStateInLifecycle()

    CrashAssistScreenContent(
        isCrashAssistEnabled = isCrashAssistEnabled,
        currentLocation = currentLocation,
        onCrashAssistChanged = { viewModel.setCrashAssistEnabled(it) },
        simulateCrash = { viewModel.simulateCrash() },
        navigateBack = { viewModel.navigateBack() }
    )
}

@Composable
private fun CrashAssistScreenContent(
    isCrashAssistEnabled: Boolean,
    currentLocation: Location?,
    onCrashAssistChanged: (Boolean) -> Unit,
    simulateCrash: () -> Unit,
    navigateBack: () -> Unit
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                action = { ToolbarBackButton(onClick = navigateBack) },
                title = stringResource(R.string.crash_assist_title)
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.margin.roomy)
        ) {
            Card(
                shape = RoundedCornerShape(AppTheme.dimens.margin.small),
                elevation = AppTheme.dimens.margin.tinier,
                modifier = Modifier.padding(vertical = AppTheme.dimens.margin.smaller),
                backgroundColor = AppTheme.colors.background.surface
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.crash_assist_title),
                        modifier = Modifier.padding(AppTheme.dimens.margin.smaller),
                        style = AppTheme.typography.title.medium
                    )

                    CrashAssistContent(isCrashAssistEnabled, currentLocation)

                    if (isCrashAssistEnabled) {
                        PrimaryButton(
                            modifier = Modifier.padding(
                                start = AppTheme.dimens.margin.smaller,
                                top = AppTheme.dimens.margin.smaller,
                                bottom = AppTheme.dimens.margin.roomy
                            ),
                            text = stringResource(R.string.crash_assist_simulate_crash_button),
                            onClick = simulateCrash,
                            buttonSize = ButtonSize.Medium
                        )
                    }
                }
            }

            Card(
                elevation = AppTheme.dimens.margin.tinier,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimens.margin.smaller),
                shape = RoundedCornerShape(AppTheme.dimens.margin.small),
                backgroundColor = AppTheme.colors.background.surface
            ) {
                SwitchRow(
                    text = stringResource(R.string.crash_assist_title),
                    checked = isCrashAssistEnabled,
                    checkedChanged = onCrashAssistChanged
                )
            }
        }
    }
}

@Composable
private fun CrashAssistContent(
    isCrashAssistEnabled: Boolean,
    currentLocation: Location?
) {
    if (isCrashAssistEnabled) {
        Box(
            modifier = Modifier
                .fillMaxHeight(FILL_MAX_HEIGHT)
                .fillMaxWidth()
        ) {
            if (currentLocation != null) {
                CrashAssistMap(currentLocation = currentLocation, modifier = Modifier.fillMaxSize())
            }
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_crash_assist_disabled),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
    val crashAssistEnableText: String
    val crashAssistEnableInfoText: String

    if (isCrashAssistEnabled) {
        crashAssistEnableText = stringResource(R.string.crash_assist_enable_text)
        crashAssistEnableInfoText = stringResource(R.string.crash_assist_enable_info)
    } else {
        crashAssistEnableText = stringResource(R.string.crash_assist_disable_text)
        crashAssistEnableInfoText = stringResource(R.string.crash_assist_disable_info)
    }

    Row {
        Text(
            text = crashAssistEnableText,
            modifier = Modifier.padding(
                start = AppTheme.dimens.margin.smaller,
                top = AppTheme.dimens.margin.smaller
            ),
            style = AppTheme.typography.title.small
        )
        if (isCrashAssistEnabled) {
            Image(
                modifier = Modifier
                    .padding(top = AppTheme.dimens.margin.smaller)
                    .size(AppTheme.dimens.iconSize.default),
                painter = painterResource(id = R.drawable.ic_shield),
                contentDescription = null
            )
        }
    }

    Text(
        text = crashAssistEnableInfoText,
        modifier = Modifier.padding(AppTheme.dimens.margin.smaller),
        style = AppTheme.typography.text.medium
    )
}

@Preview
@Composable
private fun CrashAssistScreenPreview() {
    AppTheme {
        CrashAssistScreenContent(
            isCrashAssistEnabled = true,
            currentLocation = null,
            onCrashAssistChanged = {},
            simulateCrash = {}
        ) {}
    }
}

@Preview
@Composable
private fun CrashAssistDisabledScreenPreview() {
    AppTheme {
        CrashAssistScreenContent(
            isCrashAssistEnabled = false,
            currentLocation = null,
            onCrashAssistChanged = {},
            simulateCrash = {}
        ) {}
    }
}

private const val FILL_MAX_HEIGHT = 0.4F
