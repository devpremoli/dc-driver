@file:Suppress("Deprecation")

package com.cmtelematics.cmtreferenceapp.driver

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cmtelematics.cmtreferenceapp.common.manager.PermissionManager
import com.cmtelematics.cmtreferenceapp.common.mapper.permissionChangedAction
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType.HighAccuracy
import com.cmtelematics.cmtreferenceapp.common.util.zipWithNext
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionChangeNotifierDriverImpl @Inject constructor(
    private val permissionManager: PermissionManager,
    private val localBroadcastManager: LocalBroadcastManager
) : PermissionChangeNotifierDriver {

    override suspend fun run() {
        permissionManager.state
            .map { it.filter { (permissionType, _) -> permissionType != HighAccuracy } }
            .zipWithNext { previousPermissionStates, currentPermissionStates ->
                previousPermissionStates.mapNotNull { (permissionType, previousPermissionState) ->
                    val currentPermissionState = currentPermissionStates.first { it.first == permissionType }.second
                    if (previousPermissionState != currentPermissionState) permissionType else null
                }
            }
            .collect { changedPermissions ->
                changedPermissions.forEach { permissionType ->
                    permissionType.permissionChangedAction?.let { serviceConstant ->
                        Timber.i("$permissionType is changed and SDK will be notified ($serviceConstant)")
                        val permissionIntent = Intent(serviceConstant)
                        localBroadcastManager.sendBroadcast(permissionIntent)
                    }
                }
            }
    }
}
