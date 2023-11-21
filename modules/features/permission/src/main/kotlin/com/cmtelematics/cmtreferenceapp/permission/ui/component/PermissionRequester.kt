package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cmtelematics.cmtreferenceapp.common.ui.util.OnLifecycleEvent
import com.cmtelematics.cmtreferenceapp.common.util.emptyImmutableList
import kotlinx.collections.immutable.ImmutableList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Define the state of the current permission request, triggers the related permission request.
 */
@Stable
internal interface PermissionRequester {

    /**
     * Represents the state of the current permission request, on callback of permission result will be set to true.
     */
    var isPermissionsRefreshed: Boolean

    /**
     * Triggers the permission request.
     */
    fun launchPermissionRequest()
}

@Stable
private class MutablePermissionRequester : PermissionRequester {

    val permissionRequestStarted = AtomicBoolean(false)

    private var requiredPermissions: ImmutableList<String> = emptyImmutableList()

    private var isPermissionsRefreshedState by mutableStateOf(false)

    override var isPermissionsRefreshed: Boolean
        set(value) {
            isPermissionsRefreshedState = value
        }
        get() = isPermissionsRefreshedState

    var launcher: ActivityResultLauncher<Array<String>>? = null

    override fun launchPermissionRequest() {
        launcher?.run {
            permissionRequestStarted.set(true)

            launch(requiredPermissions.toTypedArray())
        } ?: error("ActivityResultLauncher cannot be null")
    }

    fun setRequiredPermissions(requiredPermissions: ImmutableList<String>) {
        this.requiredPermissions = requiredPermissions
    }
}

@Composable
internal fun rememberPermissionRequester(requiredPermissions: ImmutableList<String>): PermissionRequester {
    val permissionState = remember { MutablePermissionRequester() }
    permissionState.setRequiredPermissions(requiredPermissions)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        permissionState.isPermissionsRefreshed = true
    }

    OnLifecycleEvent {
        if (permissionState.permissionRequestStarted.getAndSet(false)) {
            permissionState.isPermissionsRefreshed = true
        }
    }

    DisposableEffect(launcher) {
        permissionState.launcher = launcher
        onDispose {
            permissionState.launcher = null
        }
    }

    return permissionState
}
