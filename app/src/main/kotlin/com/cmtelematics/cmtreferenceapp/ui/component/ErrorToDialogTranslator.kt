package com.cmtelematics.cmtreferenceapp.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.R
import com.cmtelematics.cmtreferenceapp.driver.HardwareRequirementsDriver
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.ErrorDialog
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.AccountNotFoundException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.DuplicateEmailException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.GenericAuthenticationException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.NotAuthorizedException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.WrongAuthCodeException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.UserAlreadyAuthenticatedException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.UserNotAuthenticatedException
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager

@Suppress("ComplexMethod")
@Composable
fun TranslatedErrorDialog(
    error: Throwable,
    onDismiss: () -> Unit
) {
    val title = stringResource(R.string.error_dialog_title_generic)
    val message = when (error) {
        is DuplicateEmailException -> stringResource(id = R.string.error_dialog_duplicate_email)
        is AccountNotFoundException -> stringResource(id = R.string.error_dialog_account_not_found)
        is WrongAuthCodeException -> stringResource(id = R.string.error_dialog_wrong_auth_code)
        is TagManager.TagLinkingException -> error.message
        is NotImplementedError -> error.message
        is TripManager.TripManagerException -> error.message
        is HardwareRequirementsDriver.NoGyroInAppOnlyModeException ->
            stringResource(id = R.string.error_dialog_no_gyro_app_only)
        is NotAuthorizedException -> stringResource(id = R.string.error_dialog_user_not_authorized)
        is GenericAuthenticationException -> stringResource(
            id = R.string.error_dialog_generic_authentication,
            error.sdkErrorCode
        )
        is UserAlreadyAuthenticatedException -> stringResource(id = R.string.error_dialog_user_already_authenticated)
        is UserNotAuthenticatedException -> stringResource(id = R.string.error_dialog_user_not_authenticated)
        else -> stringResource(
            id = R.string.error_dialog_generic_with_tech_info,
            error.toString()
        )
    }?.let { errorMessage ->
        if (error is AuthenticationException && error.statusCode > 0) {
            errorMessage + " " + stringResource(id = R.string.error_additional_http_code, error.statusCode)
        } else {
            errorMessage
        }
    }

    val dismissButtonText = stringResource(
        if (error is AuthenticationException && error !is NotAuthorizedException) {
            R.string.error_dialog_try_again_generic
        } else {
            R.string.error_dialog_dismiss_generic
        }
    )

    ErrorDialog(
        title = title,
        text = message,
        dismissButtonText = dismissButtonText,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun GenericErrorDialogPreview() {
    AppTheme {
        TranslatedErrorDialog(
            error = Exception("Anything"),
            onDismiss = { }
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun DuplicateEmailErrorDialogPreview() {
    AppTheme {
        TranslatedErrorDialog(
            error = DuplicateEmailException("email", 400, Exception("Preview")),
            onDismiss = { }
        )
    }
}
