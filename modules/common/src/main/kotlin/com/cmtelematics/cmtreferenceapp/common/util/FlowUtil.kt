package com.cmtelematics.cmtreferenceapp.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.launchInLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) = flowWithLifecycle(lifecycle, minActiveState)
    .launchIn(lifecycle.coroutineScope)

@Composable
fun <T> Flow<T>.flowWithLifecycle(minActiveState: Lifecycle.State = Lifecycle.State.STARTED): Flow<T> {
    val lifecycleOwner = LocalLifecycleOwner.current

    return remember(this, lifecycleOwner, minActiveState) {
        flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
    }
}

fun <T> flowOf(factory: () -> T): Flow<T> = flow {
    emit(factory())
}

inline fun <reified T, reified R> Flow<List<T>>.mapList(crossinline transform: (T) -> R): Flow<List<R>> =
    map { list -> list.map(transform) }

@Suppress("UNCHECKED_CAST")
fun <T, R> Flow<T>.zipWithNext(transform: suspend (T, T) -> R): Flow<R> = flow {
    var prev: Any? = UNDEFINED
    collect { value ->
        if (prev !== UNDEFINED) emit(transform(prev as T, value))
        prev = value
    }
}

private object UNDEFINED
