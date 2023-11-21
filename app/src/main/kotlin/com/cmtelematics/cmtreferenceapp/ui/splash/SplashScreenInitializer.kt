package com.cmtelematics.cmtreferenceapp.ui.splash

import androidx.core.splashscreen.SplashScreen
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class SplashScreenInitializer @Inject constructor(
    private val startupManager: StartupManager,
    dispatcherProvider: DispatcherProvider
) : SplashScreen.KeepOnScreenCondition {

    private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.default)

    private val shouldKeepOnScreen = startupManager.isAppInitialised()
        .map { initialized -> initialized.not() }
        .stateIn(scope, SharingStarted.Eagerly, true)

    init {
        scope.launch {
            startupManager.initializeApp()
        }
    }

    override fun shouldKeepOnScreen(): Boolean = shouldKeepOnScreen.value
}
