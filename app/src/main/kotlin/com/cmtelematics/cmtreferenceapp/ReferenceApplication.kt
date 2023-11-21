package com.cmtelematics.cmtreferenceapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.cmtelematics.cmtreferenceapp.common.manager.DriverManager
import com.cmtelematics.cmtreferenceapp.common.util.ActivityProvider
import com.cmtelematics.cmtreferenceapp.ui.MainActivity
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.SdkManager
import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ReferenceApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var sdkManager: SdkManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var activityProvider: ActivityProvider

    @Inject
    lateinit var driverManager: DriverManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        activityProvider.initialize(this)

        sdkManager.initialize(
            application = this,
            configuration = SdkManager.SdkConfiguration(
                endpoint = BuildConfig.API_ENDPOINT,
                apiKey = BuildConfig.API_KEY,
                notificationParams = WrapperNotificationParams(
                    notificationIcon = R.mipmap.ic_launcher,
                    notificationTitle = R.string.app_name,
                    appNameRes = R.string.app_name,
                    mainActivityClass = MainActivity::class.java
                ),
                debug = BuildConfig.DEBUG
            )
        )

        registerActivityLifecycleCallbacks(SdkLifecycleNotifier(sdkManager))

        driverManager.launchDrivers()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        sdkManager.onTrimMemory(level)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.VERBOSE)
            .build()
}
