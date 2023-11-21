package com.cmtelematics.cmtreferenceapp.common.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun OnLifecycleEvent(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME,
    onEvent: () -> Unit
) {
    val currentOnEvent by rememberUpdatedState(onEvent)

    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                currentOnEvent()
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, lifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
}
