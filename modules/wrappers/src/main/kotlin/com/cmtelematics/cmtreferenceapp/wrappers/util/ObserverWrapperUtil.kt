package com.cmtelematics.cmtreferenceapp.wrappers.util

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

/**
 * Bridge function between SDK's Rx Api and Kotlin flows.
 *
 * Give an SDK function like `MyManager.observe(o: Observer)` you can use this function to get an kotlin [Flow] instead:
 *
 * ```
 *  getObserverCallbackFlow { MyManager.observe(it) }
 *    .map {}
 *    .collect {}
 * ```
 */
internal fun <T : Any> getObserverCallbackFlow(
    onNextFailure: (Throwable?) -> Unit = {},
    onError: (Throwable) -> Unit = {},
    subscriberBlock: (Observer<T>) -> Unit
) = callbackFlow {
    var disposable: Disposable? = null
    subscriberBlock(object : Observer<T> {
        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: T) {
            trySendBlocking(t)
                .onFailure { throwable ->
                    Timber.e(throwable, "Failed to deliver observer value.")
                    onNextFailure(throwable)
                }
        }

        override fun onError(e: Throwable) {
            Timber.e(e, "Failed to observe the required state.")
            onError(e)
        }

        override fun onComplete() {
            disposable?.dispose()
        }
    })
    awaitClose { disposable?.dispose() }
}
