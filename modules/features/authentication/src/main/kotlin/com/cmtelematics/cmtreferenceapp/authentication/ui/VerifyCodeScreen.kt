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
import com.cmtelematics.cmtreferenceapp.authentication.model.VerifyCodeViewModel
import com.cmtelematics.cmtreferenceapp.authentication.ui.component.TextWithEmbeddedLink
import com.cmtelematics.cmtreferenceapp.authentication.util.spannedStringResource
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield.FocusDelay
import com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield.SubmittableFocusedPlainTextField
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

private const val OTP_CODE_LENGTH = 4

@Composable
internal fun VerifyCodeScreen(viewModel: VerifyCodeViewModel = hiltViewModel()) {
    val code by viewModel.code.collectAsStateInLifecycle()
    val isSubmitEnabled by viewModel.submitEnabled.collectAsStateInLifecycle()

    VerifyCodeScreenContent(
        navigateBack = { viewModel.navigateBack() },
        code = code,
        codeChanged = { viewModel.code.value = it },
        submitEnabled = isSubmitEnabled,
        submit = { viewModel.submit() },
        resendCode = { viewModel.resendCode() }
    )
}

@Composable
private fun VerifyCodeScreenContent(
    navigateBack: () -> Unit,
    code: String,
    codeChanged: (String) -> Unit,
    submitEnabled: Boolean,
    submit: () -> Unit,
    resendCode: () -> Unit
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                action = {
                    ToolbarBackButton(
                        onClick = navigateBack
                    )
                },
                title = stringResource(id = R.string.title_existing_user)
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
                text = stringResource(R.string.verify_code_title),
                style = AppTheme.typography.title.medium
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

            SubmittableFocusedPlainTextField(
                value = code,
                onValueChange = codeChanged,
                maxLength = OTP_CODE_LENGTH,
                label = stringResource(R.string.code_label),
                placeholder = stringResource(R.string.code_placeholder),
                modifier = Modifier.fillMaxWidth(),
                // needs a slight delay since we can't request focus while the fullscreen loader is visible.
                focusDelay = FocusDelay.Short,
                submitEnabled = submitEnabled,
                submit = submit,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.small))

            if (code.isBlank()) {
                TextWithEmbeddedLink(
                    modifier = Modifier,
                    text = spannedStringResource(R.string.resend_pin_description),
                    linkAnnotation = RESEND_PIN_LINK_ANNOTATION,
                    onLinkClick = resendCode
                )
            }

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

private const val RESEND_PIN_LINK_ANNOTATION = "resendPin"

@Preview
@Composable
private fun VerifyCodeScreenPreview() {
    AppTheme {
        VerifyCodeScreenContent(
            navigateBack = {},
            code = "",
            codeChanged = {},
            submitEnabled = true,
            submit = {},
            resendCode = {}
        )
    }
}
