package com.cmtelematics.cmtreferenceapp.wrappers.crash

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDataStore<T>(initialValue: T? = null) : DataStore<T?> {

    private val dataFlow = MutableStateFlow(initialValue)

    override val data: Flow<T?> = dataFlow

    override suspend fun updateData(transform: suspend (t: T?) -> T?): T? {
        val newValue = transform(dataFlow.value)
        dataFlow.value = newValue
        return newValue
    }
}
