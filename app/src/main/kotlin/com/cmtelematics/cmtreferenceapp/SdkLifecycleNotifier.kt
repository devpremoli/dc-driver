package com.cmtelematics.cmtreferenceapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.SdkManager

/**
 * Notifies the SDK of Activity lifecycle events. Normally, you'd want the AppModel within the SDK to be attached to
 * a specific Activity within your app. This is because the SDK does backend synchronization in onResume which you
 * should only ever call when your telematics Activity is actually about to be displayed.
 *
 * This is a single Activity+Compose based app, so we don't need to worry about identifying our telematics Activity.
 * Please note that this might not be the case for your app!
 */
class SdkLifecycleNotifier(private val sdkManager: SdkManager) : Application.ActivityLifecycleCallbacks {
    override fun onActivityStarted(activity: Activity) {
        sdkManager.onActivityStarted()
    }

    override fun onActivityResumed(activity: Activity) {
        sdkManager.onActivityResumed()
    }

    override fun onActivityPaused(activity: Activity) {
        sdkManager.onActivityPaused()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        sdkManager.onActivitySaveInstanceState(outState)
    }

    override fun onActivityStopped(activity: Activity) {
        sdkManager.onActivityStopped()
    }

    override fun onActivityDestroyed(activity: Activity) {
        sdkManager.onActivityDestroyed()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // NO-OP
    }
}
