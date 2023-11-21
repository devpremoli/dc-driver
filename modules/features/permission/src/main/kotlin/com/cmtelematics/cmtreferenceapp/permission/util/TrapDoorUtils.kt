package com.cmtelematics.cmtreferenceapp.permission.util

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.cmtelematics.cmtreferenceapp.common.ui.util.requireActivity
import com.cmtelematics.cmtreferenceapp.common.util.createSettingsIntent
import java.util.concurrent.atomic.AtomicBoolean

@Composable
internal fun createOpenSettings(): (() -> Unit) =
    if (!LocalInspectionMode.current) {
        getActivity().defaultOpenSettings()
    } else {
        {}
    }

@Composable
internal fun TrapDoorBackHandler() {
    if (!LocalInspectionMode.current) {
        val activity = getActivity()
        BackHandler {
            activity.finish()
        }
    }
}

@Composable
private fun getActivity(): Activity = LocalContext.current.requireActivity()

private fun Activity.defaultOpenSettings(): () -> Unit =
    { startActivity(createSettingsIntent()) }

internal typealias TrapDoorRequestStartedState = AtomicBoolean

@Composable
internal fun rememberTrapDoorRequestStartedState(): TrapDoorRequestStartedState =
    remember { AtomicBoolean(false) }
