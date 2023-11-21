package com.cmtelematics.cmtreferenceapp.wrappers

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import kotlinx.coroutines.test.TestDispatcher

internal fun getTestDispatcherProvider(dispatcher: TestDispatcher): DispatcherProvider = object : DispatcherProvider {
    override val default = dispatcher
    override val main = dispatcher
    override val io = dispatcher
}
