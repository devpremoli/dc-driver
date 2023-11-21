package com.cmtelematics.cmtreferenceapp.wrappers.tags

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier.SharingScope
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager.TagLinkingException
import com.cmtelematics.cmtreferenceapp.wrappers.tags.model.TagConnection
import com.cmtelematics.cmtreferenceapp.wrappers.tags.model.TagConnection.Connected
import com.cmtelematics.cmtreferenceapp.wrappers.tags.model.TagConnection.Disconnected
import com.cmtelematics.cmtreferenceapp.wrappers.util.getObserverCallbackFlow
import com.cmtelematics.sdk.TagConnectionStatusObserver
import com.cmtelematics.sdk.TagStatusManager
import com.cmtelematics.sdk.VehicleTagsManager
import com.cmtelematics.sdk.types.TagConnectionStatus
import com.cmtelematics.sdk.types.TagConnectionStatus.DISCONNECTED
import com.cmtelematics.sdk.types.TagSummary
import com.cmtelematics.sdk.types.Vehicle
import com.cmtelematics.sdk.types.VehicleTagLinkingError
import com.cmtelematics.sdk.types.VehicleTagLinkingListener
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class TagManagerImpl @Inject constructor(
    private val vehicleTagsManager: VehicleTagsManager,
    private val tagStatusManager: TagStatusManager,
    @SharingScope
    private val sharingScope: CoroutineScope,
    private val tagConnectionStatusObserver: TagConnectionStatusObserver,
    private val dispatcherProvider: DispatcherProvider
) : TagManager {
    private var inProgressLinking: CancellableContinuation<Unit>? = null

    override val state: Flow<TagConnection> = combine(
        getTagConnectionStatusFlow(),
        getTagMacAddressFlow(),
        ::Pair
    ).map { (connectionStatus, macAddress) ->
        if (connectionStatus != DISCONNECTED) {
            Connected(macAddress)
        } else {
            Disconnected
        }
    }

    override suspend fun linkTag(
        vehicle: Vehicle,
        onTagDiscovered: (String) -> Unit
    ) = withContext(dispatcherProvider.main) {
        check(inProgressLinking == null) { "A linking is already in progress. Only one may be running at a time." }
        try {
            suspendCancellableCoroutine { continuation ->
                // save coroutine continuation so it can be cancelled later.
                inProgressLinking = continuation

                val listener = createLinkingListener(continuation, onTagDiscovered)
                vehicleTagsManager.beginLinkingTag(listener, vehicle)

                continuation.invokeOnCancellation { vehicleTagsManager.finishLinkingTag() }
            }
        } finally {
            inProgressLinking = null
        }
    }

    override fun confirmLinkingTag() {
        Timber.i("Tag linking confirmation requested.")
        vehicleTagsManager.confirmLinkingTag()
    }

    override fun cancelLinkingTag() {
        Timber.i("Tag linking cancel requested.")
        inProgressLinking?.cancel()
    }

    override suspend fun getTagSummaryForVehicle(vehicle: Vehicle): TagSummary? = withContext(dispatcherProvider.io) {
        vehicle.tagMacAddress?.let { tagStatusManager.getTagSummary(it) }
    }

    private fun createLinkingListener(
        continuation: CancellableContinuation<Unit>,
        onTagDiscovered: (String) -> Unit
    ) = object : VehicleTagLinkingListener {
        override fun onTagLinkingConfirm(
            tagMacAddress: String,
            supportsBeep: Boolean,
            supportsBlink: Boolean
        ) {
            Timber.i(
                "Need to confirm tag linking. Tag: %s, supports beep: %s, supports blink: %s",
                tagMacAddress,
                supportsBeep,
                supportsBlink
            )
            vehicleTagsManager.indicateLinkingTag(supportsBeep, supportsBlink)
            onTagDiscovered(tagMacAddress)
        }

        override fun onTagLinkingFailed(
            vehicleShortId: Long,
            tagMacAddress: String?,
            error: VehicleTagLinkingError?
        ) {
            Timber.e("Failed to link vehicle %d to tag '%s': %s", vehicleShortId, tagMacAddress, error)
            continuation.resumeWithException(TagLinkingException("Failed to link tag: $error", error))
        }

        override fun onTagLinkingComplete(vehicleShortId: Long, tagMacAddress: String?) {
            Timber.i("Tag linking complete for vehicle: %d. Tag: '%s'", vehicleShortId, tagMacAddress)
            vehicleTagsManager.finishLinkingTag()
            continuation.resume(Unit)
        }
    }

    private fun getTagConnectionStatusFlow(): Flow<TagConnectionStatus> =
        getObserverCallbackFlow<TagConnectionStatus> { observer ->
            tagConnectionStatusObserver.subscribe(observer)
        }.shareIn(sharingScope, SharingStarted.Lazily, replay = 1)

    private fun getTagMacAddressFlow(): Flow<String> = getObserverCallbackFlow<String> { observer ->
        tagConnectionStatusObserver.subscribeToTagMacAddress(observer)
    }.shareIn(sharingScope, SharingStarted.Lazily, replay = 1)
}
