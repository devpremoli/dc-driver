package com.cmtelematics.cmtreferenceapp.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("UnnecessaryAbstractClass")
abstract class BaseViewModel(
    protected val navigator: Navigator,
    private val errorService: ErrorService
) : ViewModel() {

    private val fullScreenLoadingCounter = MutableStateFlow(0)

    private val inlineLoadingCounter = MutableStateFlow(0)

    val fullScreenLoading = fullScreenLoadingCounter.map { it > 0 }

    val inlineLoading = inlineLoadingCounter.map { it > 0 }

    val loading = inlineLoading.stateIn(viewModelScope, WhileSubscribed(), false)

    protected suspend fun trackProgress(fullScreen: Boolean = true, action: suspend () -> Unit) {
        try {
            if (fullScreen) showFullScreenLoader() else showInlineLoader()

            action.invoke()
        } finally {
            if (fullScreen) hideFullScreenLoader() else hideInlineLoader()
        }
    }

    protected fun showFullScreenLoader() {
        fullScreenLoadingCounter.value = fullScreenLoadingCounter.value + 1
    }

    protected fun hideFullScreenLoader() {
        fullScreenLoadingCounter.value = (fullScreenLoadingCounter.value - 1).coerceAtLeast(0)
    }

    protected fun showInlineLoader() {
        inlineLoadingCounter.value = inlineLoadingCounter.value + 1
    }

    protected fun hideInlineLoader() {
        inlineLoadingCounter.value = (inlineLoadingCounter.value - 1).coerceAtLeast(0)
    }

    protected fun launchWithErrorHandling(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context) {
        try {
            block()
        } catch (t: Throwable) {
            Timber.e(t, "ViewModel block failed.")
            errorService.handle(t)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    protected suspend fun <T> runWithErrorHandling(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T
    ): T? = withContext(context) {
        try {
            block()
        } catch (t: Throwable) {
            Timber.e(t, "ViewModel block failed.")
            errorService.handle(t)
            null
        }
    }
}
