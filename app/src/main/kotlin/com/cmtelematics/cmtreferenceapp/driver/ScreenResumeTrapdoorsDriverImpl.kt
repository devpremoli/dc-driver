package com.cmtelematics.cmtreferenceapp.driver

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.factory.PermissionRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.util.Constants.isOnboardingFlowCompleted
import com.cmtelematics.cmtreferenceapp.common.util.EmptyActivityLifecycleCallbacks
import com.cmtelematics.cmtreferenceapp.common.util.zipWithNext
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.ui.MainActivity
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScreenResumeTrapdoorsDriverImpl @Inject constructor(
    private val application: Application,
    private val navigator: Navigator,
    private val permissionRouteFactory: PermissionRouteFactory,
    @SessionDataStore
    private val preferences: DataStore<Preferences>,
    private val crashManager: CrashManager,
    private val startupManager: StartupManager,
    private val permissionManager: PermissionManager
) : ScreenResumeTrapdoorsDriver {

    override suspend fun run() {
        val resumes = callbackFlow {
            application.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is MainActivity) {
                        trySend(ON_CREATE)
                    }
                }

                override fun onActivityResumed(activity: Activity) {
                    if (activity is MainActivity) {
                        trySend(ON_RESUME)
                    }
                }
            })

            awaitCancellation()
        }
            .zipWithNext { previousLifeCycleEvent, currentLifeCycleEvent ->
                previousLifeCycleEvent == ON_RESUME && currentLifeCycleEvent == ON_RESUME
            }
            .filter { it }

        val hasNoActiveCrashFlow = crashManager.activeCrash
            .map { it == null }

        val isOnboardingFlowCompletedFlow = preferences.data
            .map { it[isOnboardingFlowCompleted] }
            .filterNotNull()

        combine(
            resumes,
            startupManager.isAppInitialised(),
            hasNoActiveCrashFlow,
            isOnboardingFlowCompletedFlow
        ) { _, isAppInitialised, hasNoActiveCrash, isOnboardingFlowCompleted ->
            isAppInitialised && hasNoActiveCrash && isOnboardingFlowCompleted
        }.collect { shouldCheckPermissions ->
            if (shouldCheckPermissions) {
                permissionManager.refresh()
                permissionRouteFactory.create(shouldReturnToOriginalScreenOnFinish = true)?.let { route ->
                    navigator.navigateTo(
                        route = route,
                        skipIfAlreadyCurrentRoute = true
                    )
                }
            }
        }
    }
}
