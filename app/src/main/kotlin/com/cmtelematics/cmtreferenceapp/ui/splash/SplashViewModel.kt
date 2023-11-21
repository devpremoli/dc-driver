package com.cmtelematics.cmtreferenceapp.ui.splash

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.SplashRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val routeFactory: MainRouteFactory,
    startupManager: StartupManager,
    navigator: Navigator,
    errorService: ErrorService
) : BaseScreenViewModel(navigator, errorService) {

    val initialized = startupManager.isAppInitialised()
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = false)

    fun showStartScreen() = viewModelScope.launch {
        val shouldNavigateBack = currentRoute<SplashRoute>().value?.shouldNavigateBack == true

        if (shouldNavigateBack) {
            navigateBack()
            return@launch
        }

        navigator.navigateTo(
            route = routeFactory.createOrDefault(),
            navigationOptions = NavigationOptions(
                popUpTo = SplashRoute,
                popUpToInclusive = true
            )
        )
    }
}
