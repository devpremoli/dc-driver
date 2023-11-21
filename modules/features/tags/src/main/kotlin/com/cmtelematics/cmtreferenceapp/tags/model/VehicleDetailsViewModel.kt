package com.cmtelematics.cmtreferenceapp.tags.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.VehicleDetailsRoute
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManager
import com.cmtelematics.sdk.types.TagSummary
import com.cmtelematics.sdk.types.Vehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the vehicle details screen.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param vehicleManager used to retrieve vehicle details on open.
 * @param tagManager used to retrieve the tag's summary on open.
 */
@HiltViewModel
internal class VehicleDetailsViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val vehicleManager: VehicleManager,
    private val tagManager: TagManager
) : BaseScreenViewModel(navigator, errorService) {
    val vehicle: StateFlow<Vehicle?> = currentRouteFlow<VehicleDetailsRoute>()
        .map { loadVehicle(it.vehicleShortId) }
        .stateIn(scope = viewModelScope, started = Lazily, initialValue = null)

    val tagSummary: StateFlow<TagSummary?> = vehicle.map { vehicle -> vehicle?.let { getTagSummaryForVehicle(it) } }
        .stateIn(scope = viewModelScope, started = Lazily, initialValue = null)

    private suspend fun loadVehicle(vehicleShortId: Long): Vehicle? = runWithErrorHandling {
        vehicleManager.getVehicleByShortId(vehicleShortId)
    }

    private suspend fun getTagSummaryForVehicle(vehicle: Vehicle): TagSummary? =
        tagManager.getTagSummaryForVehicle(vehicle)
}
