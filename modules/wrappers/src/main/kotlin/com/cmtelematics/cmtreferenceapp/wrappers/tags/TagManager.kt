package com.cmtelematics.cmtreferenceapp.wrappers.tags

import com.cmtelematics.cmtreferenceapp.wrappers.tags.model.TagConnection
import com.cmtelematics.sdk.types.TagSummary
import com.cmtelematics.sdk.types.Vehicle
import com.cmtelematics.sdk.types.VehicleTagLinkingError
import kotlinx.coroutines.flow.Flow

/**
 * Provides services related to Tags and Tag linking.
 *
 * [Docs](https://my-cmt-alpha.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-tag/tag-management/tag-activation-overview/activating-tags-using-the-android-sdk.html)
 */
interface TagManager {

    /**
     * The current tag connection state of the user's tag with it's mac address.
     */
    val state: Flow<TagConnection>

    /**
     * Link a tag to the specified vehicle. This function will only return once the Tag linking process has fully
     * completed or failed, in which case a [TagLinkingException] will be thrown. The function handles its own
     * threading, there's no need to call it on any specific dispatcher.
     *
     * This function is cancellable and will gracefully abort the linking process in such a case.
     *
     * @param vehicle the vehicle to find and link a Tag to
     * @param onTagDiscovered called from the main thread once a Tag is discovered. The tag's mac address is provided
     *                        as an argument. At this point, [confirmLinkingTag] or [cancelLinkingTag] must be called,
     *                        otherwise [linkTag] will never return.
     */
    suspend fun linkTag(vehicle: Vehicle, onTagDiscovered: (String) -> Unit)

    /**
     * Confirm that the discovered Tag is the one the user wants to link to their vehicle. Should only be called once
     * [linkTag]'s callback has been invoked.
     */
    fun confirmLinkingTag()

    /**
     * Abort the current linking process. Should only be called once [linkTag]'s callback has been called.
     */
    fun cancelLinkingTag()

    /**
     * Thrown by [linkTag] if there's an issue encountered while linking to a Tag.
     *
     * @param message a user readable exception message
     * @property error the original SDK error code or null if undetermined
     */
    class TagLinkingException(message: String, val error: VehicleTagLinkingError?) : Exception(message)

    /**
     * Gets the tag summary for the specified vehicle.
     *
     * @param vehicle the vehicle to get the tag summary for
     * @return the tag summary or null if no tag info is found
     */
    suspend fun getTagSummaryForVehicle(vehicle: Vehicle): TagSummary?
}
