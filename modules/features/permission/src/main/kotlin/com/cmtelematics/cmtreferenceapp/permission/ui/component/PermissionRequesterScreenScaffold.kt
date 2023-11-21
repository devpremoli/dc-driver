package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.cmtelematics.cmtreferenceapp.common.mapper.toPermissions
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.common.util.toImmutableList

@Composable
internal fun PermissionRequesterScreenScaffold(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit,
    content: @Composable ColumnScope.(requestPermission: () -> Unit) -> Unit
) {
    val permissionRequester = rememberPermissionRequester(
        requiredPermissionType.toPermissions().toImmutableList()
    )

    PermissionsRefreshedEffect(permissionRequester, refreshPermissionStates)

    PermissionRequesterScaffoldContainer {
        content(requestPermission = {
            markAsRequested(requiredPermissionType)

            permissionRequester.launchPermissionRequest()
        })
    }
}
