package com.cmtelematics.cmtreferenceapp.service

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Suppress("InjectDispatcher")
class DispatcherProviderImpl @Inject constructor() : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.IO
}
