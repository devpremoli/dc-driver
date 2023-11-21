package com.cmtelematics.cmtreferenceapp.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.settings.R
import com.cmtelematics.cmtreferenceapp.settings.model.SettingsViewModel
import com.cmtelematics.cmtreferenceapp.settings.model.StandbyMode
import com.cmtelematics.cmtreferenceapp.settings.ui.component.StandbyModeRow
import com.cmtelematics.cmtreferenceapp.settings.ui.dialog.StandbyModeSelectorDialog
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.ConfirmDialog
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.SwitchRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextButtonRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import java.time.ZonedDateTime

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val isTripCompletionNotificationEnabled by viewModel.tripCompletionNotificationEnabled.collectAsStateInLifecycle()
    val areAudioAlertsEnabled by viewModel.audioAlertsEnabled.collectAsStateInLifecycle()
    val isUploadOnWifiOnlyEnabled by viewModel.uploadOnWifiOnlyEnabled.collectAsStateInLifecycle()
    val showSignOutDialog by viewModel.showSignOutDialog.collectAsStateInLifecycle()
    val showStandbySelectorDialog by viewModel.showStandbySelectorDialog.collectAsStateInLifecycle()
    val standbyExpirationDate by viewModel.standbyExpirationDate.collectAsStateInLifecycle()
    val isTagUser by viewModel.isTagUser.collectAsStateInLifecycle()

    SettingsScreenContent(
        tripCompletionNotificationEnabled = isTripCompletionNotificationEnabled,
        onTripCompletionNotificationChanged = { viewModel.tripCompletionNotificationEnabled.value = it },
        audioAlertsEnabled = areAudioAlertsEnabled,
        onAudioAlertsChanged = { viewModel.audioAlertsEnabled.value = it },
        uploadOnWifiOnlyEnabled = isUploadOnWifiOnlyEnabled,
        onUploadWifiOnlyChanged = { viewModel.uploadOnWifiOnlyEnabled.value = it },
        appVersion = viewModel.appVersion,
        standbyExpirationDate = standbyExpirationDate,
        showSignOutDialog = showSignOutDialog,
        cancelSignOut = { viewModel.cancelSignOut() },
        confirmSignOut = { viewModel.confirmSignOut() },
        signOut = { viewModel.signOut() },
        openDebugMenu = { viewModel.openDebugMenu() },
        copyDebugOptionValue = { viewModel.copyToClipboard(it) },
        showStandbySelectorDialog = showStandbySelectorDialog,
        isTagUser = isTagUser,
        openStandbyModeSelector = { viewModel.openStandbyModeSelector() },
        cancelStandbyModeSelector = { viewModel.cancelStandbyModeSelector() },
        selectStandbyMode = { viewModel.selectStandbyMode(it) }
    )
}

@Composable
private fun SettingsScreenContent(
    tripCompletionNotificationEnabled: Boolean?,
    onTripCompletionNotificationChanged: (Boolean) -> Unit,
    audioAlertsEnabled: Boolean?,
    onAudioAlertsChanged: (Boolean) -> Unit,
    uploadOnWifiOnlyEnabled: Boolean?,
    onUploadWifiOnlyChanged: (Boolean) -> Unit,
    appVersion: String,
    standbyExpirationDate: ZonedDateTime?,
    showSignOutDialog: Boolean,
    cancelSignOut: () -> Unit,
    confirmSignOut: () -> Unit,
    signOut: () -> Unit,
    openDebugMenu: () -> Unit,
    copyDebugOptionValue: (String) -> Unit,
    showStandbySelectorDialog: Boolean,
    isTagUser: Boolean,
    openStandbyModeSelector: () -> Unit,
    cancelStandbyModeSelector: () -> Unit,
    selectStandbyMode: (StandbyMode) -> Unit
) {
    ScreenScaffold(
        backgroundColor = AppTheme.colors.background.content,
        toolbar = { Toolbar(title = stringResource(R.string.title_settings)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.background.primary)
        ) {
            Divider(color = AppTheme.colors.divider)

            SwitchRowSection(
                tripCompletionNotificationEnabled = tripCompletionNotificationEnabled,
                onTripCompletionNotificationChanged = onTripCompletionNotificationChanged,
                audioAlertsEnabled = audioAlertsEnabled,
                onAudioAlertsChanged = onAudioAlertsChanged,
                uploadOnWifiOnlyEnabled = uploadOnWifiOnlyEnabled,
                onUploadWifiOnlyChanged = onUploadWifiOnlyChanged
            )

            if (!isTagUser) {
                StandbyModeRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = openStandbyModeSelector),
                    standbyExpirationDate = standbyExpirationDate
                )
            }

            TextButtonRow(
                rowTitle = stringResource(R.string.row_app_version),
                buttonTitle = appVersion,
                onClick = { copyDebugOptionValue(appVersion) }
            )

            TextRow(
                modifier = Modifier.clickable(onClick = openDebugMenu),
                rowTitle = stringResource(R.string.button_open_debug_menu)
            )

            TextRow(
                modifier = Modifier.clickable(onClick = signOut),
                rowTitle = stringResource(R.string.button_sign_out)
            )

            ConfirmDialog(
                showDialog = showSignOutDialog,
                cancel = cancelSignOut,
                confirm = confirmSignOut,
                title = stringResource(R.string.dialog_title_sign_out),
                text = stringResource(R.string.dialog_text_sign_out)
            )

            StandbyModeSelectorDialog(
                showDialog = showStandbySelectorDialog,
                cancel = cancelStandbyModeSelector,
                selectStandbyMode = selectStandbyMode
            )
        }
    }
}

@Composable
private fun SwitchRowSection(
    tripCompletionNotificationEnabled: Boolean?,
    onTripCompletionNotificationChanged: (Boolean) -> Unit,
    audioAlertsEnabled: Boolean?,
    onAudioAlertsChanged: (Boolean) -> Unit,
    uploadOnWifiOnlyEnabled: Boolean?,
    onUploadWifiOnlyChanged: (Boolean) -> Unit
) {
    SwitchRow(
        text = stringResource(R.string.row_trip_completion_notification),
        checked = tripCompletionNotificationEnabled == true,
        checkedChanged = onTripCompletionNotificationChanged
    )

    SwitchRow(
        text = stringResource(R.string.row_audio_alerts),
        checked = audioAlertsEnabled == true,
        checkedChanged = onAudioAlertsChanged
    )

    SwitchRow(
        text = stringResource(R.string.row_upload_wifi_only),
        checked = uploadOnWifiOnlyEnabled == true,
        checkedChanged = onUploadWifiOnlyChanged
    )
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreenContent(
            tripCompletionNotificationEnabled = true,
            onTripCompletionNotificationChanged = {},
            audioAlertsEnabled = true,
            onAudioAlertsChanged = {},
            uploadOnWifiOnlyEnabled = false,
            onUploadWifiOnlyChanged = {},
            appVersion = "1.0.0",
            standbyExpirationDate = ZonedDateTime.now(),
            showSignOutDialog = false,
            cancelSignOut = {},
            confirmSignOut = {},
            signOut = {},
            openDebugMenu = {},
            copyDebugOptionValue = {},
            showStandbySelectorDialog = false,
            isTagUser = false,
            openStandbyModeSelector = {},
            cancelStandbyModeSelector = {},
            selectStandbyMode = {}
        )
    }
}

@Preview
@Composable
private fun SettingsScreeTagUserPreview() {
    AppTheme {
        SettingsScreenContent(
            tripCompletionNotificationEnabled = true,
            onTripCompletionNotificationChanged = {},
            audioAlertsEnabled = true,
            onAudioAlertsChanged = {},
            uploadOnWifiOnlyEnabled = false,
            onUploadWifiOnlyChanged = {},
            appVersion = "1.0.0",
            standbyExpirationDate = ZonedDateTime.now(),
            showSignOutDialog = false,
            cancelSignOut = {},
            confirmSignOut = {},
            signOut = {},
            openDebugMenu = {},
            copyDebugOptionValue = {},
            showStandbySelectorDialog = false,
            isTagUser = true,
            openStandbyModeSelector = {},
            cancelStandbyModeSelector = {},
            selectStandbyMode = {}
        )
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun SettingsScreenWidthStandbySelectorDialogPreview() {
    AppTheme {
        SettingsScreenContent(
            tripCompletionNotificationEnabled = true,
            onTripCompletionNotificationChanged = {},
            audioAlertsEnabled = true,
            onAudioAlertsChanged = {},
            uploadOnWifiOnlyEnabled = false,
            onUploadWifiOnlyChanged = {},
            appVersion = "1.0.0",
            standbyExpirationDate = ZonedDateTime.now(),
            showSignOutDialog = true,
            cancelSignOut = {},
            confirmSignOut = {},
            signOut = {},
            openDebugMenu = {},
            copyDebugOptionValue = {},
            showStandbySelectorDialog = true,
            isTagUser = false,
            openStandbyModeSelector = {},
            cancelStandbyModeSelector = {},
            selectStandbyMode = {}
        )
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun SettingsScreenWidthSignOutDialogPreview() {
    AppTheme {
        SettingsScreenContent(
            tripCompletionNotificationEnabled = true,
            onTripCompletionNotificationChanged = {},
            audioAlertsEnabled = true,
            onAudioAlertsChanged = {},
            uploadOnWifiOnlyEnabled = false,
            onUploadWifiOnlyChanged = {},
            appVersion = "1.0.0",
            standbyExpirationDate = ZonedDateTime.now(),
            showSignOutDialog = true,
            cancelSignOut = {},
            confirmSignOut = {},
            signOut = {},
            openDebugMenu = {},
            copyDebugOptionValue = {},
            showStandbySelectorDialog = false,
            isTagUser = false,
            openStandbyModeSelector = {},
            cancelStandbyModeSelector = {},
            selectStandbyMode = {}
        )
    }
}
