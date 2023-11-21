package com.cmtelematics.cmtreferenceapp.permission.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionState
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal open class TrapdoorViewModel protected constructor(
    navigator: Navigator,
    errorService: ErrorService,
    permissionManager: PermissionManager,
    routeFactory: MainRouteFactory,
    requiredPermissionType: PermissionType
) : BasePermissionFlowViewModel(
    navigator = navigator,
    errorService = errorService,
    permissionManager = permissionManager,
    routeFactory = routeFactory,
    requiredPermissionType = requiredPermissionType
) {
    init {
        requiredPermissionStateFlow
            .filter { it == PermissionState.Allowed }
            .onEach { routeToNextPermission() }
            .launchIn(viewModelScope)
    }
}
