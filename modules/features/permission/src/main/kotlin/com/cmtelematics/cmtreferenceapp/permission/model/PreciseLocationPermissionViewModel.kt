package com.cmtelematics.cmtreferenceapp.permission.model

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This ViewModel handles the location based permission state refreshing.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param permissionManager Provides permissions-related function.
 * @param routeFactory Provides routing-related function.
 */
@HiltViewModel
internal class PreciseLocationPermissionViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val permissionManager: PermissionManager,
    routeFactory: MainRouteFactory
) : PermissionViewModel(
    navigator = navigator,
    errorService = errorService,
    permissionManager = permissionManager,
    routeFactory = routeFactory,
    requiredPermissionType = PermissionType.PreciseLocation
)
