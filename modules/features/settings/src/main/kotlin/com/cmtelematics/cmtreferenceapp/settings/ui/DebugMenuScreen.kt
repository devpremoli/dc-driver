package com.cmtelematics.cmtreferenceapp.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.settings.R
import com.cmtelematics.cmtreferenceapp.settings.model.DebugMenuViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider
import com.cmtelematics.cmtreferenceapp.theme.ui.component.row.TextButtonRow
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun DebugMenuScreen(viewModel: DebugMenuViewModel = hiltViewModel()) {
    val isTagUser by viewModel.isTagUser.collectAsStateInLifecycle()
    val driverId by viewModel.driverId.collectAsStateInLifecycle()
    val deviceId by viewModel.deviceId.collectAsStateInLifecycle()
    val pushToken by viewModel.pushToken.collectAsStateInLifecycle()
    val tagMacAddress by viewModel.macAddress.collectAsStateInLifecycle()
    val email by viewModel.email.collectAsStateInLifecycle()

    DebugMenuScreenContent(
        navigateBack = { viewModel.navigateBack() },
        serverUrl = viewModel.serverUrl,
        sdkVersion = viewModel.sdkVersion,
        isTagUser = isTagUser,
        isCertPinningEnabled = false,
        userId = driverId,
        deviceId = deviceId,
        tagMacAddress = tagMacAddress,
        pushToken = pushToken,
        simulateRuntimeCrash = { viewModel.crashApplication() },
        userEmail = email,
        copyDebugOptionValue = { viewModel.copyToClipboard(it) }
    )
}

@Suppress("LongMethod")
@Composable
private fun DebugMenuScreenContent(
    navigateBack: () -> Unit,
    serverUrl: String,
    sdkVersion: String,
    isTagUser: Boolean?,
    isCertPinningEnabled: Boolean,
    userId: String?,
    deviceId: String?,
    tagMacAddress: String?,
    pushToken: String?,
    simulateRuntimeCrash: () -> Unit,
    userEmail: String?,
    copyDebugOptionValue: (String) -> Unit
) {
    ScreenScaffold(
        backgroundColor = AppTheme.colors.background.content,
        toolbar = {
            Toolbar(
                action = {
                    ToolbarBackButton(
                        onClick = navigateBack
                    )
                },
                title = stringResource(R.string.title_debug_menu)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.background.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Divider(color = AppTheme.colors.divider)

            TextButtonRow(
                rowTitle = stringResource(R.string.row_server_url),
                buttonTitle = stringResource(R.string.row_server_url_button),
                onClick = { copyDebugOptionValue(serverUrl) }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_sdk_version),
                buttonTitle = sdkVersion,
                onClick = { copyDebugOptionValue(sdkVersion) }
            )

            val userType = stringResource(
                if (isTagUser == true) {
                    R.string.tag_user_type
                } else {
                    R.string.phone_user_type
                }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_tag_or_phone_user),
                buttonTitle = userType,
                onClick = { copyDebugOptionValue(userType) }
            )

            val certPinningEnabled = stringResource(
                if (isCertPinningEnabled) {
                    R.string.cert_pinning_enabled
                } else {
                    R.string.cert_pinning_disabled
                }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_cert_pinning),
                buttonTitle = certPinningEnabled,
                onClick = { copyDebugOptionValue(certPinningEnabled) }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_user_id),
                buttonTitle = userId.orEmpty(),
                onClick = { copyDebugOptionValue(userId.orEmpty()) }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_device_id),
                buttonTitle = stringResource(R.string.row_copy_button),
                onClick = { copyDebugOptionValue(deviceId.orEmpty()) }
            )

            DebugInfoText(deviceId.orEmpty())

            if (isTagUser == true) {
                TextButtonRow(
                    rowTitle = stringResource(R.string.row_mac_address_for_connected_tag),
                    buttonTitle = stringResource(R.string.row_copy_button),
                    onClick = { copyDebugOptionValue(tagMacAddress.orEmpty()) }
                )

                DebugInfoText(tagMacAddress ?: stringResource(R.string.tag_is_disconnected))
            }

            TextButtonRow(
                rowTitle = stringResource(R.string.row_push_token),
                buttonTitle = stringResource(R.string.row_copy_button),
                onClick = { copyDebugOptionValue(pushToken.orEmpty()) }
            )

            DebugInfoText(pushToken.orEmpty())

            TextButtonRow(
                rowTitle = stringResource(R.string.row_force_crash_button),
                buttonTitle = stringResource(R.string.row_force_crash_button_button),
                onClick = { simulateRuntimeCrash() }
            )

            TextButtonRow(
                rowTitle = stringResource(R.string.row_user_e_mail),
                buttonTitle = stringResource(R.string.row_copy_button),
                onClick = { copyDebugOptionValue(userEmail.orEmpty()) }
            )

            DebugInfoText(userEmail.orEmpty())
        }
    }
}

@Composable
private fun DebugInfoText(info: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimens.margin.default),
        text = info
    )
    Divider(color = AppTheme.colors.divider)
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        DebugMenuScreenContent(
            navigateBack = {},
            serverUrl = "https://verylongurloftheapplicationplusverylongurloftheapplication.com",
            sdkVersion = "2.0.9",
            isTagUser = false,
            isCertPinningEnabled = false,
            userId = "",
            deviceId = "",
            tagMacAddress = "",
            pushToken = "",
            simulateRuntimeCrash = {},
            userEmail = "",
            copyDebugOptionValue = {}
        )
    }
}
