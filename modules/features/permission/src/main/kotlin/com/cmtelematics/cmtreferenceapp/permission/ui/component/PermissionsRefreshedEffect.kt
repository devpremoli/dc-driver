package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.runtime.Composable

@Composable
internal fun PermissionsRefreshedEffect(
    requester: PermissionRequester,
    refresh: () -> Unit
) {
    if (requester.isPermissionsRefreshed) {
        requester.isPermissionsRefreshed = false
        refresh()
    }
}
