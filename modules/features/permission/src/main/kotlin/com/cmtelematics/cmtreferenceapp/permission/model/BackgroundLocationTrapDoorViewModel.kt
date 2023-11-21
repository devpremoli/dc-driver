package com.cmtelematics.cmtreferenceapp.permission.model

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
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
internal class BackgroundLocationTrapDoorViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    permissionManager: PermissionManager,
    routeFactory: MainRouteFactory
) : TrapdoorViewModel(
    navigator = navigator,
    errorService = errorService,
    permissionManager = permissionManager,
    routeFactory = routeFactory,
    requiredPermissionType = PermissionType.BackgroundLocation
)
