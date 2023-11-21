package com.cmtelematics.cmtreferenceapp.crash.model

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceCancelledRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AmbulanceCancelledViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    routeFactory: MainRouteFactory
) : BaseAmbulanceViewModel(navigator, errorService, routeFactory) {

    override val needToPopUpToOnNavigation = AmbulanceCancelledRoute
}
