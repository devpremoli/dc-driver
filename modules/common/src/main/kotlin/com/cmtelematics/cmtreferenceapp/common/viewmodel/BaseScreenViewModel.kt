package com.cmtelematics.cmtreferenceapp.common.viewmodel

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.NavigationResult
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Suppress("UnnecessaryAbstractClass")
abstract class BaseScreenViewModel(
    navigator: Navigator,
    errorService: ErrorService
) : BaseViewModel(navigator, errorService) {

    private val mutableCurrentRoute = MutableStateFlow<Route?>(null)

    private val mutableNavigationResult = MutableStateFlow<NavigationResult?>(null)

    val navigationResult: Flow<NavigationResult?>
        get() = mutableNavigationResult

    open fun navigateBack() = launchWithErrorHandling {
        navigator.navigateBack()
    }

    fun setCurrentRoute(route: Route) {
        mutableCurrentRoute.value = route
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Route> currentRoute(): StateFlow<T?> =
        mutableCurrentRoute as StateFlow<T?>

    fun <T : Route> currentRouteFlow(): Flow<T> =
        currentRoute<T>().filterNotNull()

    fun setNavigationResult(result: NavigationResult?) {
        mutableNavigationResult.value = result
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : NavigationResult> getNavigationResultAs(): Flow<T> =
        mutableNavigationResult
            .filterNotNull()
            .map { result -> result as T }
}
