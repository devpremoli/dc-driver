package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.ACTION_CRASH_DETECTED
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.EXTRA_CRASH_JSON
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

/**
 * Glue code to receive crashes from the SDK and forward them to a class that can actually do something with them.
 */
@AndroidEntryPoint
internal class CrashDetectionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var crashBroadcastManager: CrashBroadcastManager

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @DriverScope
    @Inject
    lateinit var driverScope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_CRASH_DETECTED) {
            driverScope.launch { handleCrash(intent) }
        }
    }

    private suspend fun handleCrash(intent: Intent) {
        if (authenticationManager.profile.first()?.crashAssistEnabled == true) {
            intent.getStringExtra(EXTRA_CRASH_JSON)?.let { crashString ->
                Timber.i("Received crash event: $crashString")
                try {
                    crashBroadcastManager.handleCrashFromBroadcast(json.decodeFromString(crashString))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to parse crash event.")
                }
            }
        }
    }

    companion object {
        private val json by lazy { Json { ignoreUnknownKeys = true } }
    }
}
