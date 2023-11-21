package com.cmtelematics.cmtreferenceapp.crash.model

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedNegativeRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CrashDetectedNegativeViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val routeFactory: MainRouteFactory
) : BaseScreenViewModel(navigator, errorService) {
    fun finishFlow() = launchWithErrorHandling {
        navigator.navigateTo(
            route = routeFactory.createOrDefault(),
            navigationOptions = NavigationOptions(
                popUpTo = CrashDetectedNegativeRoute,
                popUpToInclusive = true
            )
        )
    }
}
