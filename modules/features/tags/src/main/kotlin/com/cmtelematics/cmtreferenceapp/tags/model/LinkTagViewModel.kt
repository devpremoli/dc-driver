package com.cmtelematics.cmtreferenceapp.tags.model

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.LinkTagRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.tags.TagLinkedRoute
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManager
import com.cmtelematics.sdk.types.VehicleTagLinkingError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel to link a tag to a specific vehicle specified via route params.
 *
 * @property navigator Framework independent navigator.
 * @property errorService Framework independent error sink.
 */
@HiltViewModel
internal class LinkTagViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val vehicleManager: VehicleManager,
    private val tagManager: TagManager,
    private val dispatcherProvider: DispatcherProvider
) : BaseScreenViewModel(navigator, errorService) {
    private val mutableDiscoveredTag = MutableStateFlow<String?>(null)
    private val mutableStartEnabled = MutableStateFlow(true)
    private val mutableNoTagFound = MutableStateFlow(false)
    val discoveredTag: StateFlow<String?> = mutableDiscoveredTag.asStateFlow()
    val startEnabled: StateFlow<Boolean> = mutableStartEnabled.asStateFlow()
    val noTagFound: StateFlow<Boolean> = mutableNoTagFound.asStateFlow()

    fun linkTag() = launchWithErrorHandling {
        try {
            mutableStartEnabled.value = false
            linkTagThrowing()
        } catch (e: TagManager.TagLinkingException) {
            if (e.error == VehicleTagLinkingError.SCANNING_ERROR_NO_TAGS_FOUND) {
                mutableNoTagFound.value = true
            } else {
                throw e
            }
        } finally {
            mutableStartEnabled.value = true
        }
    }

    fun confirmLinkingTag() {
        mutableDiscoveredTag.value = null
        tagManager.confirmLinkingTag()
    }

    fun cancelLinkingTag() = launchWithErrorHandling {
        // The user selected "Not my tag". We need to cancel the linking process which should reenable the UI. The user
        // can restart it if they so choose.
        mutableDiscoveredTag.value = null
        tagManager.cancelLinkingTag()
    }

    fun dismissNoTagFound() {
        mutableNoTagFound.value = false
    }

    private suspend fun linkTagThrowing() = withContext(dispatcherProvider.default) {
        val vehicle = currentRoute<LinkTagRoute>().value?.run { vehicleManager.getVehicleByShortId(vehicleShortId) }
            ?: error("Could not retrieve vehicle from navigation args.")

        // Linking process starts here. linkTag will return once the process has fully completed, or will throw if
        // any errors are encountered. linkTag takes a lambda argument that gets called when a tag is discovered and
        // starts blinking. This lambda is called while linkTag is still running so it's important to call linkTag
        // on a thread/dispatcher that is off the main thread.
        tagManager.linkTag(vehicle) { discoveredTagAddress ->
            // This will trigger a confirmation dialog on the UI.
            mutableDiscoveredTag.value = discoveredTagAddress
        }

        // This point can only be reached once a tag has been successfully linked.
        navigator.navigateTo(TagLinkedRoute())
    }
}
