package com.cmtelematics.cmtreferenceapp.wrappers.authentication

import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.AccountNotFoundException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.DuplicateEmailException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.GenericAuthenticationException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.NotAuthorizedException
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationException.WrongAuthCodeException
import com.cmtelematics.sdk.types.AppServerErrorCode.ACCOUNT_NOT_FOUND
import com.cmtelematics.sdk.types.AppServerErrorCode.ALREADY_REGISTERED
import com.cmtelematics.sdk.types.AppServerErrorCode.NOT_AUTHORIZED
import com.cmtelematics.sdk.types.AppServerErrorCode.WRONG_AUTH_CODE
import com.cmtelematics.sdk.types.AppServerResponseException

sealed class AuthenticationException(
    val statusCode: Int,
    message: String,
    cause: Throwable
) : Exception(message, cause) {

    /**
     * Exception thrown when there isn't a more specific Exception available for [AppServerResponseException.errorCode].
     *
     * @param sdkErrorCode the name of the errorCode enum that the DriveWell SDK returned
     * @param statusCode the HTTP status code
     * @param cause standard exception cause param
     */
    class GenericAuthenticationException(val sdkErrorCode: String, statusCode: Int, cause: Throwable) :
        AuthenticationException(
            statusCode = statusCode,
            cause = cause,
            message = "Generic error occurred. Status code: $statusCode SDK error code: $sdkErrorCode"
        )

    /**
     * Exception thrown from [AuthenticationManager.register] when the email address specified is already in use.
     *
     * @param email the email address used to register
     * @param statusCode the HTTP status code
     * @param cause standard exception cause param
     */
    class DuplicateEmailException(email: String, statusCode: Int, cause: Throwable) :
        AuthenticationException(statusCode, "Email address '$email' already registered.", cause)

    /**
     * Exception thrown from [AuthenticationManager.verify] when the email address specified is not registered.
     *
     * @param email the email address used to log in
     * @param statusCode the HTTP status code
     * @param cause standard exception cause param
     */
    class AccountNotFoundException(email: String, statusCode: Int, cause: Throwable) :
        AuthenticationException(statusCode, "Email address '$email' not registered.", cause)

    /**
     * Exception thrown from [AuthenticationManager.loginWithOTP] when the otp is not valid.
     *
     * @param code the OTP used to log in
     * @param statusCode the HTTP status code
     * @param cause standard exception cause param
     */
    class WrongAuthCodeException(code: String, statusCode: Int, cause: Throwable) :
        AuthenticationException(statusCode, "OTP code '$code' not valid.", cause)

    /**
     * Exception thrown from [AuthenticationManager.refreshProfile] when user is in deauthorized state.
     *
     * @param statusCode the HTTP status code
     * @param cause standard exception cause param
     */
    class NotAuthorizedException(statusCode: Int, cause: Throwable) :
        AuthenticationException(statusCode, "User is not authorized.", cause)
}

/**
 * Utility function to convert a DriveWell SDK exception to a specific subtype of [AuthenticationException].
 */
internal fun AppServerResponseException.toAuthenticationException(
    additionalInfo: String? = null
): AuthenticationException = when (errorCode) {
    ALREADY_REGISTERED -> DuplicateEmailException(
        email = additionalInfo ?: error("Email must be not null"),
        statusCode = httpCode,
        cause = this
    )
    NOT_AUTHORIZED -> NotAuthorizedException(
        statusCode = httpCode,
        cause = this
    )
    WRONG_AUTH_CODE -> WrongAuthCodeException(
        code = additionalInfo ?: error("OTP must be not null"),
        statusCode = httpCode,
        cause = this
    )
    ACCOUNT_NOT_FOUND -> AccountNotFoundException(
        email = additionalInfo ?: error("Email must be not null"),
        statusCode = httpCode,
        cause = this
    )
    else -> GenericAuthenticationException(errorCode.name, httpCode, this)
}
