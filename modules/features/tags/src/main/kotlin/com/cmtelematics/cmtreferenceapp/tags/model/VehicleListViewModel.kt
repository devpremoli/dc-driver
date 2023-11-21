package com.cmtelematics.cmtreferenceapp.tags.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.CreateVehicleRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.LinkTagRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleDetailsRoute
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManager
import com.cmtelematics.sdk.types.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the vehicle list screen.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 */
@HiltViewModel
internal class VehicleListViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val vehicleManager: VehicleManager
) : BaseScreenViewModel(navigator, errorService) {

    private val mutableIsRefreshing = MutableStateFlow(false)

    val vehicles = vehicleManager.vehicleList
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = emptyList())

    val isRefreshing = mutableIsRefreshing.asStateFlow()

    fun linkTagToVehicle(vehicle: Vehicle) = launchWithErrorHandling {
        navigator.navigateTo(LinkTagRoute(vehicle.shortVehicleId))
    }

    fun addVehicle() = launchWithErrorHandling {
        navigator.navigateTo(CreateVehicleRoute())
    }

    fun refreshList() = launchWithErrorHandling {
        try {
            mutableIsRefreshing.value = true
            vehicleManager.refreshVehicleList()
        } finally {
            mutableIsRefreshing.value = false
        }
    }

    fun openVehicleDetails(vehicle: Vehicle) = launchWithErrorHandling {
        navigator.navigateTo(VehicleDetailsRoute(vehicle.shortVehicleId))
    }
}
