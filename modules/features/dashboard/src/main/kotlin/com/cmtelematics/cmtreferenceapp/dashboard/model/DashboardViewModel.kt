package com.cmtelematics.cmtreferenceapp.dashboard.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.util.Constants.isOnboardingFlowCompleted
import com.cmtelematics.cmtreferenceapp.common.util.zipWithNext
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.dashboard.HomeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManager
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val analyticsManager: AnalyticsManager,
    settingsManager: SettingsManager,
    @SessionDataStore
    private val preferences: DataStore<Preferences>
) : BaseScreenViewModel(navigator, errorService) {

    private val currentTabScreen: MutableStateFlow<Route?> = MutableStateFlow(null)
    val initialBottomNavigationRoute = HomeRoute

    init {
        currentTabScreen.zipWithNext(::Pair)
            .onEach { (previousScreen, currentScreen) ->
                logScreenNavigation(previousScreen, currentScreen)
            }
            .launchIn(viewModelScope)

        currentTabScreen.value = initialBottomNavigationRoute.create(null)

        viewModelScope.launch {
            closeOnboardingFlow()
            settingsManager.setTripSuppressionNotificationsEnabled(true)
        }
    }

    private suspend fun closeOnboardingFlow() {
        preferences.edit { it[isOnboardingFlowCompleted] = true }
    }

    private suspend fun logScreenNavigation(previousScreen: Route?, currentScreen: Route?) =
        analyticsManager.logScreenNavigation(previousScreen, currentScreen)

    fun logTabScreenNavigation(bottomNavigationRoute: BottomNavigationRoute) {
        currentTabScreen.value = bottomNavigationRoute.routeFactory.create(null)
    }
}
