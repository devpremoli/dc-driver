package com.cmtelematics.cmtreferenceapp.crash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.model.CrashDetectedNegativeViewModel
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashScreenScaffold
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashSubTitleText
import com.cmtelematics.cmtreferenceapp.crash.ui.component.CrashTitleText
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun CrashDetectedNegativeScreen(
    viewModel: CrashDetectedNegativeViewModel = hiltViewModel()
) {
    CrashDetectedNegativeScreenContent(
        onClickDone = { viewModel.finishFlow() }
    )
}

@Composable
private fun CrashDetectedNegativeScreenContent(
    onClickDone: () -> Unit
) {
    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.large))

    CrashScreenScaffold(
        primaryButtonClicked = onClickDone,
        primaryButtonText = stringResource(R.string.crash_detected_negative_done_button)
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        CrashTitleText(text = stringResource(R.string.crash_detected_negative_title))

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraTiny))

        CrashSubTitleText(text = stringResource(R.string.crash_detected_negative_sub_title))

        Image(
            modifier = Modifier
                .weight(1f)
                .align(CenterHorizontally),
            painter = painterResource(R.drawable.ic_thumbs_up),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun CrashDetectedNegativeScreenPreview() {
    AppTheme {
        CrashDetectedNegativeScreenContent(
            onClickDone = {}
        )
    }
}
