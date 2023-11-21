package com.cmtelematics.cmtreferenceapp.crash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.model.AmbulanceCancelledViewModel
import com.cmtelematics.cmtreferenceapp.crash.ui.component.AmbulanceScreenContent
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun AmbulanceCancelledScreen(
    viewModel: AmbulanceCancelledViewModel = hiltViewModel()
) {
    val shouldShowReportAClaimInfoDialog by viewModel.shouldShowReportAClaimInfoDialog.collectAsStateInLifecycle()
    val shouldShowRequestATowInfoDialog by viewModel.shouldShowRequestATowInfoDialog.collectAsStateInLifecycle()

    AmbulanceCancelledScreenContent(
        shouldShowReportAClaimInfoDialog = shouldShowReportAClaimInfoDialog,
        shouldShowRequestATowInfoDialog = shouldShowRequestATowInfoDialog,
        reportAClaim = { viewModel.showReportAClaimInfoDialog() },
        requestATow = { viewModel.showRequestATowInfoDialog() },
        finishFlow = { viewModel.finishFlow() }
    )
}

@Composable
private fun AmbulanceCancelledScreenContent(
    shouldShowReportAClaimInfoDialog: Boolean,
    shouldShowRequestATowInfoDialog: Boolean,
    reportAClaim: () -> Unit,
    requestATow: () -> Unit,
    finishFlow: () -> Unit
) = AmbulanceScreenContent(
    title = stringResource(R.string.crash_ambulance_cancelled_title),
    shouldShowReportAClaimInfoDialog = shouldShowReportAClaimInfoDialog,
    shouldShowRequestATowInfoDialog = shouldShowRequestATowInfoDialog,
    reportAClaim = reportAClaim,
    requestATow = requestATow,
    finishFlow = finishFlow
)

@Preview
@Composable
private fun AmbulanceCancelledScreenPreview() {
    AppTheme {
        AmbulanceCancelledScreenContent(
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
private fun AmbulanceCancelledScreenWithDialogPreview() {
    AppTheme {
        AmbulanceCancelledScreenContent(
            shouldShowReportAClaimInfoDialog = true,
            shouldShowRequestATowInfoDialog = false,
            reportAClaim = {},
            requestATow = {},
            finishFlow = {}
        )
    }
}
