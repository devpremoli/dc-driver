package com.cmtelematics.cmtreferenceapp.tags.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the vehicle creation form.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 * @property vehicleManager used to create the vehicle via the SDK.
 */
@HiltViewModel
internal class CreateVehicleViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val vehicleManager: VehicleManager
) : BaseScreenViewModel(navigator, errorService) {
    val vehicleNickname = MutableStateFlow("")
    val vehicleMake = MutableStateFlow("")
    val vehicleModel = MutableStateFlow("")
    val isSubmitEnabled = vehicleNickname.map { it.isNotEmpty() }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = false)

    fun submit() = launchWithErrorHandling {
        trackProgress {
            vehicleManager.addVehicle(
                nickname = vehicleNickname.value,
                make = vehicleMake.value.takeIf { it.isNotEmpty() },
                model = vehicleModel.value.takeIf { it.isNotEmpty() }
            )
            navigateBack()
        }
    }
}
