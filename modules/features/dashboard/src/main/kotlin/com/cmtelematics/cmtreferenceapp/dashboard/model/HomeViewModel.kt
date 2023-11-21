package com.cmtelematics.cmtreferenceapp.dashboard.model

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashAssistRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleListRoute
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    authenticationManager: AuthenticationManager,
    tripManager: TripManager
) : BaseScreenViewModel(navigator, errorService) {

    val isTagUser: Flow<Boolean> = authenticationManager.profile.map { it?.isTagUser == true }
    val isBatteryLowForTripRecording: StateFlow<Boolean> = tripManager.batteryIsLowForTripRecording

    fun openCrashFeature() = launchWithErrorHandling {
        navigator.navigateTo(CrashAssistRoute())
    }

    fun openVehicles() = launchWithErrorHandling {
        navigator.navigateTo(VehicleListRoute())
    }
}
