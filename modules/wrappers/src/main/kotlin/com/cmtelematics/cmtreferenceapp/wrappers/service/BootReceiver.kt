package com.cmtelematics.cmtreferenceapp.wrappers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

/**
 * We register this boot receiver so the app is launched on device boot. It however doesn't need to do anything as
 * the drivers will handle all followup tasks.
 */
@AndroidEntryPoint
internal class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @DriverScope
    @Inject
    lateinit var driverScope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // NO-OP: crash notification manager acts as a driver and automatically recreates alarms.
            Timber.i("Boot event received.")
        }
    }
}
