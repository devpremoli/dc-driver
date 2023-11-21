package com.cmtelematics.cmtreferenceapp.common.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

private const val TICKER_INTERVAL = 500L

fun getTicker(interval: Long = TICKER_INTERVAL) = flow {
    while (true) {
        emit(Unit)
        delay(interval)
    }
}
