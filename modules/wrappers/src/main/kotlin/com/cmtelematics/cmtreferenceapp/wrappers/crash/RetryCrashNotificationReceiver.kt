package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManager.RetryNotificationType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Handles user taps on crash notifications.
 */
@AndroidEntryPoint
internal class RetryCrashNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var crashNotificationManager: CrashNotificationManager

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action in validActions) {
            Timber.i("Received a crash notification retry event.")
            crashNotificationManager.handleNotificationRetryAlarmFired()
        }
    }

    companion object {
        private val validActions by lazy { RetryNotificationType.values().map { it.action } }
    }
}
