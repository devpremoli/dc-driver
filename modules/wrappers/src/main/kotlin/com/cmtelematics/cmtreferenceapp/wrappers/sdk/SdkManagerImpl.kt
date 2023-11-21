@file:Suppress("Deprecation")

package com.cmtelematics.cmtreferenceapp.wrappers.sdk

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cmtelematics.cmtreferenceapp.wrappers.R
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashDetectionReceiver
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.RetryCrashNotificationReceiver
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.InternalConstants.ACTION_CRASH_DETECTED
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.cmtreferenceapp.wrappers.manager.WrapperDriverManager
import com.cmtelematics.sdk.AppAnalyticsManager
import com.cmtelematics.sdk.AppModel
import com.cmtelematics.sdk.CmtActivityLifecycleCallbacks
import com.cmtelematics.sdk.Sdk
import com.cmtelematics.sdk.ServiceNotificationReceiver.CMT_WARNING_CHANNEL
import com.cmtelematics.sdk.types.Configuration
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

private val isInitialized = AtomicBoolean(false)

@Suppress("LongParameterList")
@Singleton
internal class SdkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationParamsHolder: WrapperNotificationParamsHolder,
    private val notificationManager: NotificationManager,
    // made Lazy, so DI resolution can happen after Sdk.init
    private val appConfiguration: Lazy<Configuration>,
    private val appAnalyticsManager: AppAnalyticsManager,
    private val localBroadcastManager: LocalBroadcastManager,
    private val wrapperDriverManager: WrapperDriverManager
) : SdkManager, AppModelProvider {

    private var privateAppModel: AppModel? = null

    override val appModel: AppModel
        get() = privateAppModel ?: error("Sdk was not initialized before attempting to get AppModel.")

    override fun initialize(application: Application, configuration: SdkManager.SdkConfiguration) {
        if (isInitialized.getAndSet(true)) error("An SdkManager is already initialized.")

        Sdk.init(context, ReferenceAppServiceConfiguration(context, configuration))

        if (configuration.debug) {
            appConfiguration.get().enableVerboseLogcat(true)
            appConfiguration.get().enableToasts(true)
        }

        notificationParamsHolder.notificationParams = configuration.notificationParams
        privateAppModel = AppModel(context)

        val callbacks = CmtActivityLifecycleCallbacks(appAnalyticsManager)
        application.registerActivityLifecycleCallbacks(callbacks)

        setupWarningChannel()
        registerCrashDetectionReceiver()
        registerCrashDetectionRetryReceiver()

        wrapperDriverManager.launchDrivers()
    }

    override fun onActivityStarted() {
        appModel.onStart()
    }

    override fun onActivityResumed() {
        appModel.onResume()
    }

    override fun onActivityPaused() {
        appModel.onPause()
    }

    override fun onActivitySaveInstanceState(outState: Bundle) {
        appModel.onSaveInstanceState(outState)
    }

    override fun onActivityStopped() {
        appModel.onStop()
    }

    override fun onActivityDestroyed() {
        appModel.onDestroy()
    }

    override fun onTrimMemory(level: Int) {
        appModel.onTrimMemory(level)
    }

    private fun setupWarningChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.warning_channel_name)
            val description = context.getString(R.string.warning_channel_description)
            val channel = NotificationChannel(CMT_WARNING_CHANNEL, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun registerCrashDetectionReceiver() = localBroadcastManager
        .registerReceiver(
            CrashDetectionReceiver(),
            IntentFilter(ACTION_CRASH_DETECTED)
        )

    private fun registerCrashDetectionRetryReceiver() = localBroadcastManager
        .registerReceiver(
            RetryCrashNotificationReceiver(),
            IntentFilter().apply {
                CrashNotificationManager.RetryNotificationType.values().forEach { type ->
                    addAction(type.action)
                }
            }
        )
}
