package com.cmtelematics.cmtreferenceapp.common.manager

import com.cmtelematics.cmtreferenceapp.common.model.PermissionState
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import kotlinx.coroutines.flow.Flow

/**
 * Handles the OS permissions needed for the DriveWell sdk to work properly.
 */
interface PermissionManager {

    /**
     * Holds the current state of the permissions as an observable stream. Most important permissions are at
     * the front of the list.
     *
     * This is a cached state. To refresh it, call [refresh]
     */
    val state: Flow<List<Pair<PermissionType, PermissionState>>>

    /**
     * This method refreshes the cached permissions exposed by the [state] property.
     */
    suspend fun refresh()

    /**
     * Tells the permission manager that the provided [PermissionType] has requested at least once.
     *
     * @param[permissionType] the requested permission type.
     */
    suspend fun markAsRequested(permissionType: PermissionType)
}
