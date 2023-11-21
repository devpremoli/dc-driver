package com.cmtelematics.cmtreferenceapp.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.common.util.ActivityProvider
import com.cmtelematics.cmtreferenceapp.ui.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityProviderImpl @Inject constructor() : ActivityProvider, Application.ActivityLifecycleCallbacks {

    private val activityReference = MutableStateFlow<WeakReference<Activity?>>(WeakReference(null))

    override fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    override suspend fun awaitActivity(): Activity = activityReference
        .map { it.get() }
        .filterNotNull()
        .first()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is MainActivity) {
            activityReference.value = WeakReference(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        // no-op
    }

    override fun onActivityResumed(activity: Activity) {
        // no-op
    }

    override fun onActivityPaused(activity: Activity) {
        // no-op
    }

    override fun onActivityStopped(activity: Activity) {
        // no-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // no-op
    }

    override fun onActivityDestroyed(activity: Activity) {
        val currentActivity = activityReference.value.get()

        if (currentActivity != null && currentActivity == activity) {
            activityReference.value = WeakReference(null)
        }
    }
}
