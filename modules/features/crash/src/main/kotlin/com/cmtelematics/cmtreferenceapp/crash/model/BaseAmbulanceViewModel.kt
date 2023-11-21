package com.cmtelematics.cmtreferenceapp.crash.model

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseAmbulanceViewModel(
    navigator: Navigator,
    errorService: ErrorService,
    private val routeFactory: MainRouteFactory
) : BaseScreenViewModel(navigator, errorService) {
    private val mutableShouldShowReportAClaimInfoDialog = MutableStateFlow(false)
    private val mutableShouldShowRequestATowInfoDialog = MutableStateFlow(false)

    protected abstract val needToPopUpToOnNavigation: Route.Factory<*>

    val shouldShowReportAClaimInfoDialog = mutableShouldShowReportAClaimInfoDialog.asStateFlow()
    val shouldShowRequestATowInfoDialog = mutableShouldShowRequestATowInfoDialog.asStateFlow()

    fun showReportAClaimInfoDialog() {
        mutableShouldShowReportAClaimInfoDialog.value = true
    }

    fun showRequestATowInfoDialog() {
        mutableShouldShowRequestATowInfoDialog.value = true
    }

    fun finishFlow() = launchWithErrorHandling {
        navigator.navigateTo(
            route = routeFactory.createOrDefault(),
            navigationOptions = NavigationOptions(
                popUpTo = needToPopUpToOnNavigation,
                popUpToInclusive = true
            )
        )
    }
}
