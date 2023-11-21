package com.cmtelematics.cmtreferenceapp.crash.model

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.service.ToastService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.crash.provider.LocationProvider
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class CrashAssistViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    locationProvider: LocationProvider,
    private val authenticationManager: AuthenticationManager,
    private val crashManager: CrashManager,
    private val toastService: ToastService
) : BaseScreenViewModel(navigator, errorService) {
    val isCrashAssistEnabled: Flow<Boolean> = authenticationManager.profile.map { it?.crashAssistEnabled == true }

    val currentLocation: StateFlow<Location?> = locationProvider
        .requestLocationUpdates()
        .map { it.lastLocation }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = getDefaultLocation())

    fun setCrashAssistEnabled(crashEscalationOptIn: Boolean) = launchWithErrorHandling {
        authenticationManager.setCrashAssistEnabled(crashEscalationOptIn)
    }

    /**
     * Simulates a fake crash after a 15 seconds of delay to test notification by backgrounding
     * the app within 15 seconds
     */
    fun simulateCrash() = launchWithErrorHandling {
        if (isCrashAssistEnabled.first()) {
            toastService.toastShort(R.string.simulate_crash_with_delay)
            delay(SIMULATE_CRASH_DELAY_SECONDS.seconds)
            crashManager.simulateCrash()
        }
    }

    companion object {
        private const val defaultLatitude = 26.5024
        private const val defaultLongitude = 83.7791
        private const val SIMULATE_CRASH_DELAY_SECONDS = 15

        private fun getDefaultLocation(): Location {
            val location = Location(LocationManager.GPS_PROVIDER)
            val sydney = LatLng(defaultLatitude, defaultLongitude)
            location.latitude = sydney.latitude
            location.longitude = sydney.longitude
            return location
        }
    }
}
