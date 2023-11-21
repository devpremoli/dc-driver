package com.cmtelematics.cmtreferenceapp.permission.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.BaseOnboardingRoute
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal open class BasePermissionFlowViewModel protected constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val permissionManager: PermissionManager,
    private val routeFactory: MainRouteFactory,
    val requiredPermissionType: PermissionType
) : BaseScreenViewModel(navigator, errorService) {

    protected val requiredPermissionStateFlow = permissionManager.state
        .map { list ->
            val (_, permissionState) = list.first { (permissionType, _) ->
                permissionType == requiredPermissionType
            }
            permissionState
        }

    fun refreshPermissionStates() = viewModelScope.launch {
        permissionManager.refresh()
    }

    protected suspend fun routeToNextPermission() = viewModelScope.launch {
        val shouldReturnToOriginalScreenOnFinish =
            currentRoute<BaseOnboardingRoute>().value?.shouldReturnToOriginalScreenOnFinish
                ?: error("shouldNavigateBackOnFinishToAppFlow must not be null")

        routeFactory.create(shouldReturnToOriginalScreenOnFinish).let { route ->
            if (route == null && shouldReturnToOriginalScreenOnFinish) {
                navigator.navigateBack()
            } else {
                navigator.navigateTo(
                    route = route ?: routeFactory.createOrDefault(),
                    navigationOptions = NavigationOptions(
                        popUpTo = currentRouteFlow<Route>().first().factory,
                        popUpToInclusive = true
                    )
                )
            }
        }
    }
}
