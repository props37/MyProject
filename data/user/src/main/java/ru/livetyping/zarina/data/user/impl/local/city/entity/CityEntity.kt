package ru.livetyping.zarina.data.user.impl.local.city.entity

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import java.io.InputStream
import java.io.OutputStream

@Serializable
internal data class CityEntity(
    val name: String,
    val id: String,
    val fullName: String?,
    val region: String?,
) {
    fun toCity(): City {
        return City(
            name = name,
            id = KladrId(id),
            fullName = fullName,
            region = region,
        )
    }

    class DataStoreSerializer(
        private val json: Json,
        private val dispatcher: CoroutineDispatcher,
    ) : Serializer<CityEntity?> {
        override val defaultValue: CityEntity? = null

        override suspend fun readFrom(input: InputStream): CityEntity? {
            return try {
                val bytes = input.readBytes()
                val string = bytes.decodeToString()
                json.decodeFromString(string)
            } catch (e: SerializationException) {
                throw CorruptionException("Failed to read stored data", e)
            }
        }

        override suspend fun writeTo(t: CityEntity?, output: OutputStream) {
            val string = json.encodeToString(t)
            val bytes = string.encodeToByteArray()
            withContext(dispatcher) {
                output.write(bytes)
            }
        }
    }

    companion object {
        fun from(city: City): CityEntity {
            return CityEntity(
                name = city.name,
                id = city.id.value,
                fullName = city.fullName,
                region = city.region,
            )
        }
    }
}
