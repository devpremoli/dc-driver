package com.cmtelematics.cmtreferenceapp.crash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.model.AmbulanceRequestedViewModel
import com.cmtelematics.cmtreferenceapp.crash.ui.component.AmbulanceScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun AmbulanceRequestedScreen(
    viewModel: AmbulanceRequestedViewModel = hiltViewModel()
) {
    val shouldShowReportAClaimInfoDialog by viewModel.shouldShowReportAClaimInfoDialog.collectAsStateInLifecycle()
    val shouldShowRequestATowInfoDialog by viewModel.shouldShowRequestATowInfoDialog.collectAsStateInLifecycle()

    AmbulanceRequestedScreenContent(
        shouldShowReportAClaimInfoDialog = shouldShowReportAClaimInfoDialog,
        shouldShowRequestATowInfoDialog = shouldShowRequestATowInfoDialog,
        reportAClaim = { viewModel.showReportAClaimInfoDialog() },
        requestATow = { viewModel.showRequestATowInfoDialog() },
        finishFlow = { viewModel.finishFlow() }
    )
}

@Composable
private fun AmbulanceRequestedScreenContent(
    shouldShowReportAClaimInfoDialog: Boolean,
    shouldShowRequestATowInfoDialog: Boolean,
    reportAClaim: () -> Unit,
    requestATow: () -> Unit,
    finishFlow: () -> Unit
) = AmbulanceScreenContent(
    title = stringResource(R.string.crash_ambulance_requested_title),
    subTitle = stringResource(R.string.crash_ambulance_requested_sub_title),
    shouldShowReportAClaimInfoDialog = shouldShowReportAClaimInfoDialog,
    shouldShowRequestATowInfoDialog = shouldShowRequestATowInfoDialog,
    reportAClaim = reportAClaim,
    requestATow = requestATow,
    finishFlow = finishFlow
)

@Preview
@Composable
private fun AmbulanceRequestedScreenPreview() {
    AppTheme {
        AmbulanceRequestedScreenContent(
            shouldShowReportAClaimInfoDialog = false,
            shouldShowRequestATowInfoDialog = false,
            reportAClaim = {},
            requestATow = {},
            finishFlow = {}
        )
    }
}

@Preview
@Composable
private fun AmbulanceRequestedScreenWithDialogPreview() {
    AppTheme {
        AmbulanceRequestedScreenContent(
            shouldShowReportAClaimInfoDialog = true,
            shouldShowRequestATowInfoDialog = false,
            reportAClaim = {},
            requestATow = {},
            finishFlow = {}
        )
    }
}
