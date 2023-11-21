package com.cmtelematics.cmtreferenceapp.permission.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.permission.PreciseAndBackgroundLocationTrapDoorRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel handles the location based permission state refreshing for trapdoor feature.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param permissionManager Provides permissions-related function.
 * @param routeFactory Provides routing-related function.
 */
@HiltViewModel
internal class PreciseAndBackgroundLocationTrapDoorViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val permissionManager: PermissionManager,
    private val routeFactory: MainRouteFactory
) : BaseScreenViewModel(
    navigator = navigator,
    errorService = errorService
) {
    private val areRequiredPermissionsAllowed = permissionManager.state
        .map { list ->
            list.filter { (permissionType, _) -> permissionType in REQUIRED_PERMISSIONS }
                .all { (_, permissionType) -> permissionType == PermissionState.Allowed }
        }

    init {
        areRequiredPermissionsAllowed
            .filter { it }
            .onEach { routeToNextPermission() }
            .launchIn(viewModelScope)
    }

    fun refreshPermissionStates() = viewModelScope.launch {
        permissionManager.refresh()
    }

    private suspend fun routeToNextPermission() {
        navigator.navigateTo(
            route = routeFactory.createOrDefault(),
            navigationOptions = NavigationOptions(
                popUpTo = PreciseAndBackgroundLocationTrapDoorRoute,
                popUpToInclusive = true
            )
        )
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            listOf(PermissionType.PreciseLocation, PermissionType.BackgroundLocation)
    }
}
