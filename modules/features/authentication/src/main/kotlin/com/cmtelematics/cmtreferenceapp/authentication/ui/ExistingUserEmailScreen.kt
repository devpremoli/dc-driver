package com.cmtelematics.cmtreferenceapp.authentication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.authentication.R
import com.cmtelematics.cmtreferenceapp.authentication.model.ExistingUserEmailViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield.SubmittableFocusedPlainTextField
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun ExistingUserEmailScreen(viewModel: ExistingUserEmailViewModel = hiltViewModel()) {
    val email by viewModel.email.collectAsStateInLifecycle()
    val isSubmitEnabled by viewModel.submitEnabled.collectAsStateInLifecycle()

    ExistingUserEmailScreenContent(
        navigateBack = { viewModel.navigateBack() },
        email = email,
        emailChanged = { viewModel.email.value = it },
        submitEnabled = isSubmitEnabled,
        submit = { viewModel.submit() }
    )
}

@Composable
private fun ExistingUserEmailScreenContent(
    navigateBack: () -> Unit,
    email: String,
    emailChanged: (String) -> Unit,
    submitEnabled: Boolean,
    submit: () -> Unit
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                action = {
                    ToolbarBackButton(
                        onClick = navigateBack
                    )
                },
                title = stringResource(R.string.title_existing_user)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimens.margin.bigger),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.huge))

            Text(
                text = stringResource(R.string.register_email_title),
                style = AppTheme.typography.title.medium
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

            SubmittableFocusedPlainTextField(
                value = email,
                onValueChange = emailChanged,
                label = stringResource(R.string.email_label),
                placeholder = stringResource(R.string.email_placeholder),
                modifier = Modifier.fillMaxWidth(),
                submitEnabled = submitEnabled,
                submit = submit,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.authentication_next),
                onClick = submit,
                enabled = submitEnabled,
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))
        }
    }
}

@Preview
@Composable
private fun ExistingUserEmailScreenContentPreview() {
    AppTheme {
        ExistingUserEmailScreenContent(
            navigateBack = {},
            email = "",
            emailChanged = {},
            submitEnabled = true,
            submit = {}
        )
    }
}
