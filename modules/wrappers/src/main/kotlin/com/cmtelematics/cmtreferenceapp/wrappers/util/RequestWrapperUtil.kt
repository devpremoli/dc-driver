package com.cmtelematics.cmtreferenceapp.wrappers.util

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.sdk.types.AppServerResponse
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Suspending coroutine wrapper around the SDK's Rx APIs. See [RequestScope] for example usage.
 */
internal suspend fun <T : AppServerResponse> sendRequest(
    dispatcherProvider: DispatcherProvider,
    block: RequestScope<T>.() -> Unit
): T = innerSendRequest(dispatcherProvider) { observer, cancellableContinuation ->
    val scope = RequestScopeImpl(observer, cancellableContinuation)
    scope.block()
    scope.validate()
}

@DslMarker
annotation class RequestScopeMarker

/**
 * A scope within which you can call you backend API that expects an Observer<T> to function.
 *
 * In this scope you MUST call [runRequest] EXACTLY once and use the provided [observer] property, also EXACTLY ONCE.
 *
 * Example usage:
 * ```
 * sendRequest {
 *   runRequest(myRequest(observer))
 * }
 * ```
 */
@RequestScopeMarker
internal interface RequestScope<T : AppServerResponse> {
    /**
     * An observer you can provide to the backend API method. Throws exception if accessed more than once.
     */
    val observer: Observer<T>

    /**
     * Run the request. You should call this method only once.
     */
    fun runRequest(result: Unit)

    /**
     * Run the request. You should call this method only once.
     */
    fun runRequest(result: Boolean, exceptionOnNegativeResult: (() -> Throwable)? = null)
}

private class RequestScopeImpl<T : AppServerResponse>(
    observer: Observer<T>,
    val continuation: CancellableContinuation<T>
) : RequestScope<T> {
    private var observerUsed = false
    private var requestRun = false

    override val observer = observer
        get() {
            check(!observerUsed) { "Observer should only be used once." }
            observerUsed = true
            return field
        }

    override fun runRequest(result: Unit) {
        check(!requestRun) { "Only one request should be run at a time." }
        requestRun = true
    }

    override fun runRequest(result: Boolean, exceptionOnNegativeResult: (() -> Throwable)?) {
        check(!requestRun) { "Only one request should be run at a time." }
        requestRun = true
        if (!result) {
            continuation.resumeWithException(exceptionOnNegativeResult?.invoke() ?: Exception("TODO"))
        }
    }

    fun validate() {
        check(requestRun) { "You must use runRequest exactly once." }
        check(observerUsed) { "You must use the observer." }
    }
}

private suspend fun <T : AppServerResponse> innerSendRequest(
    dispatcherProvider: DispatcherProvider,
    block: (Observer<T>, CancellableContinuation<T>) -> Unit
) =
    withContext(dispatcherProvider.io) {
        suspendCancellableCoroutine { continuation ->
            var disposable: Disposable? = null
            val observer = object : Observer<T> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: T) {
                    continuation.resume(t)
                    disposable?.dispose()
                }

                override fun onError(e: Throwable) {
                    continuation.resumeWithException(e)
                }

                override fun onComplete() {
                    Timber.d("SDK request completed")
                }
            }

            block(observer, continuation)

            continuation.invokeOnCancellation { disposable?.dispose() }
        }
    }
