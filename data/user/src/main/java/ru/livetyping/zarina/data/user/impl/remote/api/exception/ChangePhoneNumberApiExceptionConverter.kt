package ru.livetyping.zarina.data.user.impl.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import ru.livetyping.zarina.core.network.di.NetworkJson
import ru.livetyping.zarina.data.user.impl.remote.api.dto.ChangePhoneNumberErrorDto
import javax.inject.Inject

internal class ChangePhoneNumberApiExceptionConverter @Inject constructor(
    @NetworkJson
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun convert(e: ResponseException): Nothing {
        val responseText = e.response.bodyAsText()
        val element = json.parseToJsonElement(responseText)
        when (element) {
            is JsonArray -> handleJsonArray(element, e)
            else -> throw e
        }
    }

    private fun handleJsonArray(element: JsonArray, originalException: Exception): Nothing {
        val errorDtos = json.decodeFromJsonElement(
            deserializer = ListSerializer(ChangePhoneNumberErrorDto.serializer()),
            element = element,
        )
        val exceptions = errorDtos.map { it.toException() }
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
            else -> throw originalException
        }
    }
}
