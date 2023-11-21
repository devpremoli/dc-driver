package com.cmtelematics.cmtreferenceapp.crash.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog.CrashInfoDialog
import com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog.InfoDialogType.REPORT_A_CLAIM
import com.cmtelematics.cmtreferenceapp.crash.ui.component.dialog.InfoDialogType.REQUEST_A_TOW
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun AmbulanceScreenContent(
    title: String,
    subTitle: String? = null,
    shouldShowReportAClaimInfoDialog: Boolean,
    shouldShowRequestATowInfoDialog: Boolean,
    reportAClaim: () -> Unit,
    requestATow: () -> Unit,
    finishFlow: () -> Unit
) {
    CrashScreenScaffold(
        primaryButtonClicked = reportAClaim,
        primaryButtonText = stringResource(R.string.crash_ambulance_report_a_claim_button),
        secondaryButtonClicked = requestATow,
        secondaryButtonText = stringResource(R.string.crash_ambulance_request_a_tow_button)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

            CrashTitleText(text = title)

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraTiny))

            subTitle?.let { subTitle ->
                CrashSubTitleText(text = subTitle)
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

            AMBULANCE_ASSISTANCE_POINTS.forEachIndexed { index, assistancePoint ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))
                }

                AssistancePointView(
                    iconResource = (index + 1).asAssistancePointIcon,
                    title = stringResource(assistancePoint.titleRes),
                    description = stringResource(assistancePoint.descriptionRes)
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

        when {
            shouldShowReportAClaimInfoDialog -> REPORT_A_CLAIM
            shouldShowRequestATowInfoDialog -> REQUEST_A_TOW
            else -> null
        }?.let { infoDialogType ->
            CrashInfoDialog(infoDialogType = infoDialogType) {
                finishFlow()
            }
        }
    }
}

private val AMBULANCE_ASSISTANCE_POINTS = listOf(
    AssistancePoint(
        titleRes = R.string.crash_assistance_tip_one,
        descriptionRes = R.string.crash_assistance_tip_one_description
    ),
    AssistancePoint(
        titleRes = R.string.crash_assistance_tip_two,
        descriptionRes = R.string.crash_assistance_tip_two_description
    ),
    AssistancePoint(
        titleRes = R.string.crash_assistance_tip_three,
        descriptionRes = R.string.crash_assistance_tip_three_description
    )
)

private data class AssistancePoint(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
)

@get:DrawableRes
private val Int.asAssistancePointIcon
    get() = when (this) {
        1 -> R.drawable.ic_crash_list_item_1
        2 -> R.drawable.ic_crash_list_item_2
        3 -> R.drawable.ic_crash_list_item_3
        else -> throw IllegalArgumentException("Unhandled assistance point index: $this")
    }

@Preview
@Composable
private fun AmbulanceScreenContentPreview() {
    AppTheme {
        AmbulanceScreenContent(
            title = "Title",
            subTitle = "Subtitle",
            shouldShowReportAClaimInfoDialog = false,
            shouldShowRequestATowInfoDialog = false,
            reportAClaim = {},
            requestATow = {},
            finishFlow = {}
        )
    }
}
