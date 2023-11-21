package com.cmtelematics.cmtreferenceapp.wrappers.authentication

import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.model.Profile
import com.cmtelematics.sdk.types.CoreProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * A manager that provides services and data related to user authentication, authorization and profile settings.
 */
interface AuthenticationManager {
    /**
     * The current authentication state of the user.
     */
    val state: Flow<AuthenticationState>

    /**
     * The **CACHED** value for the profile of the user. Automatically refreshed from the server during the login
     * process and upon app restart. Otherwise call [refreshProfile] to refresh from the backend. This is backed by a
     * flow with replay. A new subscriber will always receive a value upon subscription. However, keep in mind that
     * the cached value can still be null.
     */
    val profile: Flow<Profile?>

    /**
     * Register a new user with the backend.
     *
     * @param email is the email address the user wants to use.
     * @param tagUser true if the user will use App+Tag, false if the app should run in App Only mode.
     * @return the profile of the newly created user
     */
    suspend fun register(email: String, tagUser: Boolean): CoreProfile

    /**
     * Verify the email address of an existing user and send an email with OTP.
     *
     * @param email is the email address the user wants to use.
     */
    suspend fun verify(email: String)

    /**
     * Verifies the OTP of the user and also logs into the application.
     *
     * @param email is the email address the user wants to use.
     * @param otp is the otp code the user was given.
     * @return The profile of the user.
     */
    suspend fun loginWithOTP(email: String, otp: String): CoreProfile

    /**
     * Refreshes the value in [profile] from the server.
     */
    suspend fun refreshProfile()

    /**
     *  Update "crashAssistEnabled" value in [profile] object based on switch action.
     */
    suspend fun setCrashAssistEnabled(newValue: Boolean)

    /**
     * Log the user out of the SDK & Backend.
     */
    suspend fun logout()

    /**
     * Authentication state of the user.
     */
    sealed interface AuthenticationState {
        /**
         * The user is logged in.
         *
         * @property shortUserId the user's short ID as per the SDK.
         */
        data class LoggedIn(val shortUserId: Long) : AuthenticationState

        /**
         * There is no user logged in currently.
         */
        object LoggedOut : AuthenticationState
    }
}

suspend fun AuthenticationManager.isLoggedIn() = state.first() is LoggedIn
