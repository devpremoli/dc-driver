package com.cmtelematics.cmtreferenceapp.crash.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.model.CrashDetectedCountDownViewModel
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CircularCountDownTimer
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashScreenScaffold
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashSubTitleText
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashTitleText
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.ConfirmDialog

@Composable
internal fun CrashDetectedCountDownScreen(
    viewModel: CrashDetectedCountDownViewModel = hiltViewModel()
) {
    val shouldShowAmbulanceConfirmDialog by viewModel.shouldShowAmbulanceConfirmDialog.collectAsStateInLifecycle()
    val remainingSeconds by viewModel.remainingSeconds.collectAsStateInLifecycle()
    val progressPercentage by viewModel.remainingCountDownTimeProgress.collectAsStateInLifecycle()

    CrashDetectedCountDownScreenContent(
        shouldShowAmbulanceConfirmDialog = shouldShowAmbulanceConfirmDialog,
        remainingCountDownTimeProgress = progressPercentage,
        remainingCountDownTime = remainingSeconds,
        showAmbulanceConfirmDialog = { viewModel.showAmbulanceConfirmDialog() },
        callAmbulance = { viewModel.callAmbulance() },
        cancelAmbulance = { viewModel.cancelAmbulance() },
        crashDetectedNegative = { viewModel.crashDetectedNegative() }
    )
}

@Composable
private fun CrashDetectedCountDownScreenContent(
    shouldShowAmbulanceConfirmDialog: Boolean,
    remainingCountDownTimeProgress: Float,
    remainingCountDownTime: Int,
    showAmbulanceConfirmDialog: () -> Unit,
    callAmbulance: () -> Unit,
    cancelAmbulance: () -> Unit,
    crashDetectedNegative: () -> Unit
) {
    CrashScreenScaffold(
        primaryButtonText = stringResource(R.string.button_yes),
        primaryButtonClicked = showAmbulanceConfirmDialog,
        secondaryButtonText = stringResource(R.string.button_no),
        secondaryButtonClicked = crashDetectedNegative
    ) {
        ConfirmDialog(
            showDialog = shouldShowAmbulanceConfirmDialog,
            cancel = cancelAmbulance,
            confirm = callAmbulance,
            title = stringResource(R.string.ambulance_confirm_dialog_title),
            text = stringResource(R.string.ambulance_confirm_dialog_message)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        CrashTitleText(text = stringResource(R.string.crash_detected_title))

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraTiny))

        CrashSubTitleText(text = stringResource(R.string.crash_detected_sub_title))

        CircularCountDownTimer(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.margin.larger)
                .weight(1f),
            remainingCountDownTime = remainingCountDownTime,
            remainingCountDownTimeProgress = remainingCountDownTimeProgress
        )
    }
}

@Preview
@Composable
private fun CrashDetectedCountDownScreenPreview() {
    AppTheme {
        CrashDetectedCountDownScreenContent(
            shouldShowAmbulanceConfirmDialog = false,
            remainingCountDownTimeProgress = 0.5F,
            remainingCountDownTime = 30,
            showAmbulanceConfirmDialog = {},
            callAmbulance = {},
            cancelAmbulance = {},
            crashDetectedNegative = {}
        )
    }
}

@Preview
@Composable
private fun CrashDetectedCountDownAmbulanceDialogPreview() {
    AppTheme {
        CrashDetectedCountDownScreenContent(
            shouldShowAmbulanceConfirmDialog = true,
            remainingCountDownTimeProgress = 0.5F,
            remainingCountDownTime = 30,
            showAmbulanceConfirmDialog = {},
            callAmbulance = {},
            cancelAmbulance = {},
            crashDetectedNegative = {}
        )
    }
}
