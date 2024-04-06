package ru.livetyping.zarina.data.product.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.data.common.remote.api.exception.ApiExceptionConverter
import ru.livetyping.zarina.data.product.remote.api.dto.SubscribeToProductEmailErrorDto
import ru.livetyping.zarina.data.product.remote.api.dto.SubscribeToProductErrorDtoSerializer
import ru.livetyping.zarina.data.product.remote.api.dto.SubscribeToProductFirstNameErrorDto
import ru.livetyping.zarina.domain.user.exception.InvalidEmailException
import ru.livetyping.zarina.domain.user.exception.InvalidFirstNameException
import javax.inject.Inject

class SubscribeToProductApiExceptionConverter @Inject constructor(
    private val json: Json,
) : ApiExceptionConverter {

    override suspend fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            val responseText = e.response.bodyAsText()
            val errorDto =
                json.decodeFromString(SubscribeToProductErrorDtoSerializer(), responseText)
            when (errorDto) {
                is SubscribeToProductEmailErrorDto -> throw InvalidEmailException()
                is SubscribeToProductFirstNameErrorDto -> throw InvalidFirstNameException()
            }
        }
    }
}
