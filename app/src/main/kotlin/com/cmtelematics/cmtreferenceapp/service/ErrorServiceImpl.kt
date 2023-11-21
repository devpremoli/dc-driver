package com.cmtelematics.cmtreferenceapp.service

import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ErrorServiceImpl @Inject constructor() : ErrorService {

    private val errors = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)

    override fun handle(throwable: Throwable) {
        errors.tryEmit(throwable)
    }

    override fun dispatch(): Flow<Throwable> = errors
}
