package com.cmtelematics.cmtreferenceapp.settings.model

import android.content.ClipboardManager
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.behavior.StandbyModeBehavior
import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.StandbySettingsManager
import com.cmtelematics.cmtreferenceapp.common.service.AppVersionProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.service.ToastService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.settings.DebugMenuRoute
import com.cmtelematics.cmtreferenceapp.settings.util.copyToClipboard
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param appVersionProvider needed to display the app version on the settings screen.
 * @param authenticationManager needed to provide the logout function.
 * @param settingsManager needed to implement push configuration, and provides opportunity to modify way of data upload.
 * @param clipboard needed to copy the required text to system clipboard.
 * @param toastService needed to show Toast.
 * @param clock needed to provide the current system time.
 * @param alertManager needed to provide voice alert alert functionality.
 */
@Suppress("LongParameterList")
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    appVersionProvider: AppVersionProvider,
    private val authenticationManager: AuthenticationManager,
    private val settingsManager: SettingsManager,
    private val clipboard: ClipboardManager,
    private val toastService: ToastService,
    override val clock: Clock,
    private val alertManager: AlertManager
) : BaseScreenViewModel(navigator, errorService), StandbyModeBehavior {
    override val standbySettingsManager: StandbySettingsManager = settingsManager

    private val mutableShowSignOutDialog = MutableStateFlow(false)
    private val mutableShowStandbySelectorDialog = MutableStateFlow(false)

    val tripCompletionNotificationEnabled = MutableStateFlow<Boolean?>(null)
    val audioAlertsEnabled = MutableStateFlow<Boolean?>(null)
    val uploadOnWifiOnlyEnabled = MutableStateFlow<Boolean?>(null)
    val showSignOutDialog = mutableShowSignOutDialog.asStateFlow()
    val showStandbySelectorDialog = mutableShowStandbySelectorDialog.asStateFlow()
    val appVersion: String = appVersionProvider.appVersionWithBuildNumber

    val standbyExpirationDate = standbySettingsManager.standbyExpirationDate
        .map { it?.atZone(clock.zone) }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val isTagUser: StateFlow<Boolean> = authenticationManager.profile
        .filterNotNull()
        .map { it.isTagUser }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = false)

    init {
        viewModelScope.launch {
            tripCompletionNotificationEnabled.value = settingsManager.getTripCompletionNotificationsEnabled()
            uploadOnWifiOnlyEnabled.value = settingsManager.getUploadOnWifiOnlyEnabled()
            audioAlertsEnabled.value = alertManager.state.first().all { (_, enabled) -> enabled }
        }

        tripCompletionNotificationEnabled
            .filterNotNull()
            .onEach { enableTripCompletionNotifications(it) }
            .catch { errorService.handle(it) }
            .launchIn(viewModelScope)

        uploadOnWifiOnlyEnabled
            .filterNotNull()
            .onEach { settingsManager.setUploadOnWifiOnlyEnabled(it) }
            .catch { errorService.handle(it) }
            .launchIn(viewModelScope)

        audioAlertsEnabled
            .filterNotNull()
            .onEach { enableAudioAlerts(it) }
            .catch { errorService.handle(it) }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            standbySettingsManager.refreshStandbyMode()
            observeStandbyExpirationDateChange()
        }
    }

    fun signOut() {
        mutableShowSignOutDialog.value = true
    }

    fun confirmSignOut() = launchWithErrorHandling {
        trackProgress {
            authenticationManager.logout()
        }
    }

    fun cancelSignOut() {
        mutableShowSignOutDialog.value = false
    }

    private suspend fun enableTripCompletionNotifications(enabled: Boolean) {
        settingsManager.setTripCompletionNotificationsEnabled(enabled)
    }

    private suspend fun enableAudioAlerts(enabled: Boolean) {
        alertManager.enableAudioAlerts(enabled = enabled)
    }

    fun openDebugMenu() = launchWithErrorHandling {
        navigator.navigateTo(DebugMenuRoute())
    }

    fun openStandbyModeSelector() {
        mutableShowStandbySelectorDialog.value = true
    }

    fun cancelStandbyModeSelector() {
        mutableShowStandbySelectorDialog.value = false
    }

    fun selectStandbyMode(standbyMode: StandbyMode) = launchWithErrorHandling {
        standbySettingsManager.setStandByModeDuration(standbyMode.standByTimeInMinutes)
        standbySettingsManager.refreshStandbyMode()
        cancelStandbyModeSelector()
    }

    fun copyToClipboard(text: String) = copyToClipboard(
        clipboard = clipboard,
        toastService = toastService,
        text = text
    )
}
