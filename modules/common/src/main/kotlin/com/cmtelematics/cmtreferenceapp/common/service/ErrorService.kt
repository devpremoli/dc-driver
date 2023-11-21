package com.cmtelematics.cmtreferenceapp.common.service

import kotlinx.coroutines.flow.Flow

interface ErrorService {

    fun handle(throwable: Throwable)

    fun dispatch(): Flow<Throwable>
}
