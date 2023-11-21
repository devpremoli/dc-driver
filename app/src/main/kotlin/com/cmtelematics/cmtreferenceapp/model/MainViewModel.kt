package com.cmtelematics.cmtreferenceapp.model

import androidx.lifecycle.ViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    suspend fun logScreenNavigation(previousScreen: Route?, currentScreen: Route?) =
        analyticsManager.logScreenNavigation(previousScreen, currentScreen)
}
