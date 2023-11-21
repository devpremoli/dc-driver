package com.cmtelematics.cmtreferenceapp.welcome.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ContainedButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.image.PoweredByLogo
import com.cmtelematics.cmtreferenceapp.theme.ui.component.image.ReferenceAppLogo
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.ReferenceAppTitle
import com.cmtelematics.cmtreferenceapp.welcome.R
import com.cmtelematics.cmtreferenceapp.welcome.model.WelcomeViewModel

@Composable
internal fun WelcomeScreen(viewModel: WelcomeViewModel = hiltViewModel()) {
    WelcomeScreenContent(
        navigateToRegisterScreen = { viewModel.navigateToRegisterEmailScreen() },
        navigateToExistingUserEmailScreen = { viewModel.navigateToExistingUserEmailScreen() }
    )
}

// The welcome screen does not follow the rest of the design system, so custom colors are OK here.
@Composable
private fun WelcomeScreenContent(
    navigateToRegisterScreen: () -> Unit,
    navigateToExistingUserEmailScreen: () -> Unit
) {
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

            Box(
                modifier = Modifier.padding(
                    top = AppTheme.dimens.margin.default,
                    start = AppTheme.dimens.margin.bigger,
                    end = AppTheme.dimens.margin.bigger
                )
            ) {
                NewUserButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = navigateToRegisterScreen
                )
            }

            ReturningUserButton(
                modifier = Modifier
                    .padding(
                        vertical = AppTheme.dimens.margin.default,
                        horizontal = AppTheme.dimens.margin.bigger
                    )
                    .fillMaxWidth(),
                onClick = navigateToExistingUserEmailScreen
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

@Composable
private fun NewUserButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = WelcomeButton(
    modifier = modifier,
    text = stringResource(id = R.string.new_user),
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = AppTheme.colors.background.primary,
        contentColor = AppTheme.colors.secondary,
        disabledBackgroundColor = AppTheme.colors.button.contained.disabledBackground,
        disabledContentColor = AppTheme.colors.button.contained.disabledContent
    )
)

@Composable
fun ReturningUserButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        modifier = modifier.defaultMinSize(minHeight = AppTheme.dimens.button.minHeightLarge),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.button.text.background,
            contentColor = AppTheme.colors.button.text.content,
            disabledBackgroundColor = AppTheme.colors.button.text.disabledBackground,
            disabledContentColor = AppTheme.colors.button.text.disabledContent
        ),
        contentPadding = PaddingValues(
            horizontal = AppTheme.dimens.button.horizontalPaddingLarge,
            vertical = AppTheme.dimens.button.verticalPaddingLarge
        ),
        shape = AppTheme.shapes.button
    ) {
        Text(
            text = stringResource(id = R.string.returning_user_question),
            color = AppTheme.colors.button.contained.content
        )
    }
}

@Composable
private fun WelcomeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors? = null
) {
    ContainedButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        buttonSize = ButtonSize.Large,
        shape = AppTheme.shapes.button,
        colors = colors
    )
}

@Preview
@Composable
private fun WelcomeScreenContentPreview() {
    AppTheme {
        WelcomeScreenContent(
            navigateToRegisterScreen = { },
            navigateToExistingUserEmailScreen = { }
        )
    }
}
