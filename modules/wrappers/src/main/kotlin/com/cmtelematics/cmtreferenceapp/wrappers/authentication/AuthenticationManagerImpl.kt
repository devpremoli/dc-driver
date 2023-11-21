@file:Suppress("SwallowedException")

package com.cmtelematics.cmtreferenceapp.wrappers.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedOut
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.model.ExtraProfileFields
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.model.Profile
import com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier.SharingScope
import com.cmtelematics.cmtreferenceapp.wrappers.util.getObserverCallbackFlow
import com.cmtelematics.cmtreferenceapp.wrappers.util.sendRequest
import com.cmtelematics.sdk.PassThruRequester
import com.cmtelematics.sdk.UserManager
import com.cmtelematics.sdk.types.AppServerResponseException
import com.cmtelematics.sdk.types.AuthPinRequest
import com.cmtelematics.sdk.types.CoreProfile
import com.cmtelematics.sdk.types.RegisterResponse
import com.cmtelematics.sdk.types.RequestPinRequest
import com.cmtelematics.sdk.types.RequestPinResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

/**
 * [Docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/integrating-the-drivewell-sdk--android-/authenticating-and-managing-users--android-.html)
 */
internal class AuthenticationManagerImpl @Inject constructor(
    private val userManager: UserManager,
    @SharingScope
    private val sharingScope: CoroutineScope,
    @DriverScope
    private val driverScope: CoroutineScope,
    private val errorService: ErrorService,
    @SessionDataStore
    private val dataStore: DataStore<Preferences>,
    private val passThruRequester: PassThruRequester,
    private val dispatcherProvider: DispatcherProvider
) : AuthenticationManager {
    private val mutableProfile = MutableSharedFlow<Profile?>(replay = 1)

    override val state: Flow<AuthenticationState> = getAuthenticationSharedFlow()

    override val profile: Flow<Profile?> = mutableProfile.asSharedFlow()

    init {
        sharingScope.launch {
            updateInitialValueForProfile()
        }

        launchLogoutObserver()
        launchProfileSyncToDisk()
    }

    private fun launchLogoutObserver() {
        // clear profile fields on logout
        state
            .map { it is LoggedIn }
            .distinctUntilChanged()
            .filter { !it }
            .onEach {
                mutableProfile.emit(null)
            }
            .launchIn(driverScope)
    }

    private fun launchProfileSyncToDisk() {
        // continually persist profile info to disk
        mutableProfile
            .onEach { profile ->
                dataStore.edit { preferences ->
                    if (profile != null) {
                        val json = withContext(dispatcherProvider.default) { Json.encodeToString(profile) }
                        preferences[profilePrefsKey] = json
                    } else {
                        preferences -= profilePrefsKey
                    }
                }
            }
            .launchIn(driverScope)
    }

    override suspend fun register(email: String, tagUser: Boolean): CoreProfile {
        val profile = CoreProfile().apply {
            this.email = email
            this.tagUser = tagUser
        }

        return try {
            sendRequest<RegisterResponse<CoreProfile>>(dispatcherProvider) {
                runRequest(
                    result = userManager.register(profile, observer),
                    exceptionOnNegativeResult = { UserAlreadyAuthenticatedException() }
                )
            }.profile
                .also {
                    // Refreshing profile via network is required here,
                    // because at this point CoreProfile object does not contain rawBody field.
                    refreshProfile()
                }
        } catch (e: AppServerResponseException) {
            throw e.toAuthenticationException(email)
        }
    }

    override suspend fun verify(email: String) {
        val request = RequestPinRequest.withEmail(email)

        try {
            sendRequest<RequestPinResponse>(dispatcherProvider) {
                runRequest(
                    result = userManager.requestPin(request, observer),
                    exceptionOnNegativeResult = { UserAlreadyAuthenticatedException() }
                )
            }
        } catch (e: AppServerResponseException) {
            throw e.toAuthenticationException(email)
        }
    }

    override suspend fun loginWithOTP(email: String, otp: String): CoreProfile {
        val request = AuthPinRequest(otp).withEmail(email)

        return try {
            sendRequest(dispatcherProvider) {
                runRequest(
                    result = userManager.authenticatePin(request, observer),
                    exceptionOnNegativeResult = { UserAlreadyAuthenticatedException() }
                )
            }
                .profile
                .also {
                    // Refreshing profile via network is required here,
                    // because at this point CoreProfile object does not contain rawBody field.
                    refreshProfile()
                }
        } catch (e: AppServerResponseException) {
            throw e.toAuthenticationException(otp)
        }
    }

    override suspend fun logout() {
        sendRequest(dispatcherProvider) {
            runRequest(
                result = userManager.deauthorize(observer),
                exceptionOnNegativeResult = { UserNotAuthenticatedException() }
            )
        }
    }

    override suspend fun refreshProfile() = try {
        val coreProfile = sendRequest(dispatcherProvider) {
            runRequest(
                result = userManager.getProfile(observer),
                exceptionOnNegativeResult = { UserNotAuthenticatedException() }
            )
        }
        mutableProfile.emit(coreProfile.mapToProfile())
    } catch (e: AppServerResponseException) {
        throw e.toAuthenticationException()
    }

    /**
     * There's no SDK API to set Crash Assist opt-in. Therefore, we have to use the PassThru API to set this value.
     * See the [docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/adding-features-for-the-drivewell-sdk--android-/using-passthrough-apis-with-the-drivewell-sdk--android-.html).
     */
    override suspend fun setCrashAssistEnabled(newValue: Boolean) {
        val jsonString = withContext(dispatcherProvider.default) {
            val profileField = ExtraProfileFields(crashAssistEnabled = newValue)
            val json = Json { encodeDefaults = true }
            json.encodeToString(profileField)
        }

        sendRequest<CoreProfile>(dispatcherProvider) {
            runRequest(
                passThruRequester.post(
                    PROFILE_ENDPOINT,
                    jsonString,
                    String::class.java,
                    CoreProfile::class.java,
                    observer
                )
            )
        }
        refreshProfile()
    }

    /**
     * Load the user Profile if the user was logged in. This should only be done once per app startup. If there's
     * a network failure we'll try to fall back to the persisted version.
     */
    private suspend fun updateInitialValueForProfile() {
        try {
            if (state.first() is LoggedIn) {
                refreshProfile()
            } else {
                mutableProfile.emit(null)
            }
        } catch (e: Exception) {
            errorService.handle(e)
            // can only catch an exception if the user was logged in. Let's see if we can retrieve the profile.
            val profile = dataStore.data.first()[profilePrefsKey]?.let {
                withContext(dispatcherProvider.default) { Json.decodeFromString<Profile>(it) }
            }
            mutableProfile.emit(profile)
        }
    }

    /**
     * The SDK already provides an observable login state, let's convert it to coroutines.
     */
    private fun getAuthenticationSharedFlow(): Flow<AuthenticationState> =
        getObserverCallbackFlow<Long>(
            onNextFailure = { Timber.e(it, "Failed to deliver auth state.") },
            onError = { Timber.e(it, "Failed to observe auth state.") }
        ) { userManager.subscribe(it) }
            .map { userId -> if (userId > 0) LoggedIn(shortUserId = userId) else LoggedOut }
            .shareIn(sharingScope, SharingStarted.Lazily, replay = 1)

    /**
     * Convert the SDK's CoreProfile class to our own class that we can safely persist. We also only keep fields we
     * are interested in.
     */
    private suspend fun CoreProfile.mapToProfile(): Profile {
        val json = Json { ignoreUnknownKeys = true }
        val crashAssistEnabled = rawBody?.let {
            val profileRawBody: ExtraProfileFields = withContext(dispatcherProvider.default) {
                json.decodeFromString(rawBody)
            }
            profileRawBody.crashAssistEnabled
        } == true

        return Profile(
            isTagUser = tagUser,
            email = email,
            crashAssistEnabled = crashAssistEnabled
        )
    }

    companion object {
        private val profilePrefsKey = stringPreferencesKey("PROFILE_PREF")
        private const val PROFILE_ENDPOINT = "/mobile/v3/update_profile"
    }
}
