package com.cmtelematics.cmtreferenceapp.settings.model

import android.content.ClipboardManager
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SdkVersion
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.ServerUrl
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.service.ToastService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.settings.util.copyToClipboard
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.device.DeviceManager
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager
import com.cmtelematics.cmtreferenceapp.wrappers.tags.model.TagConnection.Connected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 *
 * @param navigator Framework independent navigator.
 * @param errorService Framework independent error sink.
 * @param authenticationManager needed to provide the logout function.
 * @param serverUrl Server url of the application.
 * @param sdkVersion Used DriveWell SDK version name.
 * @param clipboard needed to copy the required text to system clipboard.
 * @param toastService needed to show toast.
 * @param deviceManager needed to obtain device related data.
 * @param deviceSettingsManager needed to obtain push token.
 * @param tagManager needed to observe the state of currently connected tag.
 */
@Suppress("LongParameterList")
@HiltViewModel
internal class DebugMenuViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    authenticationManager: AuthenticationManager,
    @ServerUrl val serverUrl: String,
    @SdkVersion val sdkVersion: String,
    private val clipboard: ClipboardManager,
    private val toastService: ToastService,
    deviceManager: DeviceManager,
    deviceSettingsManager: SettingsManager,
    tagManager: TagManager
) : BaseScreenViewModel(navigator, errorService) {
    val isTagUser: StateFlow<Boolean?> = authenticationManager.profile.map { it?.isTagUser }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val driverId: StateFlow<String?> = authenticationManager.state
        .map { (it as? LoggedIn)?.run { shortUserId.toString() } }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val email: StateFlow<String?> = authenticationManager.profile.map { it?.email }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val deviceId: StateFlow<String?> = deviceManager.deviceIdentifier
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val pushToken: StateFlow<String?> = deviceSettingsManager.pushToken
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    val macAddress: StateFlow<String?> = tagManager.state
        .map { (it as? Connected)?.macAddress }
        .stateIn(scope = viewModelScope, started = WhileSubscribed(), initialValue = null)

    @Suppress("TooGenericExceptionThrown")
    fun crashApplication() {
        throw RuntimeException("App crash test")
    }

    fun copyToClipboard(text: String) = copyToClipboard(
        clipboard = clipboard,
        toastService = toastService,
        text = text
    )
}
