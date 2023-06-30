package ru.zarina.zarina.utils.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

open class Serializer<T> internal constructor(
    private val defaultValueProducer: () -> T,
    private val decodeFromString: (String) -> T,
    private val encodeToString: (T) -> String,
) : Serializer<T> {
    override val defaultValue: T
        get() = defaultValueProducer()


    override suspend fun readFrom(input: InputStream): T {
        try {
            val bytes = input.readBytes()
            val string = bytes.decodeToString()
            return decodeFromString(string)
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read stored data", e)
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val string = encodeToString(t)
        val bytes = string.encodeToByteArray()
        withContext(Dispatchers.IO) {
            output.write(bytes)
        }
    }
}
