package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cmtelematics.cmtreferenceapp.common.ui.util.OnLifecycleEvent
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.util.TrapDoorRequestStartedState
import com.cmtelematics.cmtreferenceapp.permission.util.createOpenSettings
import com.cmtelematics.cmtreferenceapp.permission.util.rememberTrapDoorRequestStartedState
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton

@Composable
internal fun TrapDoorScreenScaffold(
    refreshPermissionStates: () -> Unit,
    permissionRequestStarted: TrapDoorRequestStartedState = rememberTrapDoorRequestStartedState(),
    openPermissionSpecificSettings: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val openSettings by rememberUpdatedState(
        createOpenSettings()
    )

    OnLifecycleEvent {
        if (permissionRequestStarted.getAndSet(false)) {
            refreshPermissionStates()
        }
    }

    PermissionRequesterScaffoldContainer {
        content()

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.permission_fix_the_problem_button),
            onClick = {
                permissionRequestStarted.set(true)

                openPermissionSpecificSettings?.invoke() ?: openSettings()
            },
            buttonSize = ButtonSize.Large
        )
    }
}
