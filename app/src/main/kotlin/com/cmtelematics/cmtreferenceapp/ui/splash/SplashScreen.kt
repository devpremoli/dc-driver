package com.cmtelematics.cmtreferenceapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.image.PoweredByLogo
import com.cmtelematics.cmtreferenceapp.theme.ui.component.image.ReferenceAppLogo
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.ReferenceAppTitle

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    val isInitialized by viewModel.initialized.collectAsStateInLifecycle()

    if (isInitialized) {
        viewModel.showStartScreen()
    }

    ScreenScaffold(
        modifier = Modifier.background(AppTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReferenceAppLogo(
                modifier = Modifier
                    .padding(top = AppTheme.dimens.margin.large)
                    .widthIn(max = 200.dp)
                    .wrapContentHeight()
            )

            ReferenceAppTitle(
                modifier = Modifier
                    .padding(
                        top = AppTheme.dimens.margin.bigRoomy,
                        start = AppTheme.dimens.margin.larger,
                        end = AppTheme.dimens.margin.larger
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            PoweredByLogo(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.margin.huge)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.roomy))
        }
    }
}
