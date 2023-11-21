package com.cmtelematics.cmtreferenceapp.wrappers.driver

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Lazy
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * [Docs](https://my.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/integrating-the-drivewell-sdk--android-/enabling-push-notifications--android-.html)
 */
@Singleton
internal class FirebaseIdDriverImpl @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val deviceSettingsManager: SettingsManager,
    private val firebase: Lazy<FirebaseMessaging>,
    private val dispatcherProvider: DispatcherProvider
) : FirebaseIdDriver {
    override suspend fun run() {
        authenticationManager.state
            .filterIsInstance<LoggedIn>()
            .conflate()
            .collect { updateFirebaseId() }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun updateFirebaseId() = withContext(dispatcherProvider.default) {
        Timber.i("User logged in, updating Firebase ID.")
        try {
            updateFirebaseIdThrowing()
        } catch (e: Exception) {
            Timber.w(e, "Fetching FCM registration token failed.")
        }
    }

    private suspend fun updateFirebaseIdThrowing() {
        val token = suspendCoroutine<String> { continuation ->
            firebase.get().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get new FCM registration token
                    val result = task.result

                    // Log and toast
                    Timber.d("Token is: $result")
                    continuation.resume(result)
                } else {
                    continuation.resumeWithException(task.exception ?: error("Exception should be non-null."))
                }
            }
        }

        deviceSettingsManager.setFirebaseToken(token)
    }
}
