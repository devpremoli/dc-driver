package com.cmtelematics.cmtreferenceapp.common.util

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

/**
 * Convert a kotlin.serialization serializer into one that androidx datastore can use. This variant allows the
 * serializer to handle null values. Read errors are automatically converted into the [defaultValue].
 */
fun <T> KSerializer<T>.toNullableSerializer(defaultValue: T? = null): Serializer<T?> = object : Serializer<T?> {
    override val defaultValue: T? = defaultValue

    override suspend fun readFrom(input: InputStream): T? = try {
        decodeFromString(this@toNullableSerializer, input.readBytes().decodeToString())
    } catch (serialization: SerializationException) {
        Timber.e(serialization, "Unable to deserialize JSON %s", this@toNullableSerializer.descriptor.serialName)
        defaultValue
    }

    // Suppressed. Coroutine context is defined by the datastore.
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: T?, output: OutputStream) {
        if (t != null) {
            output.write(
                Json.encodeToString(this@toNullableSerializer, t)
                    .encodeToByteArray()
            )
        }
    }
}
