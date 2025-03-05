package ru.livetyping.zarina.data.user.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.data.user.remote.api.dto.ChangePhoneNumberErrorDto
import ru.livetyping.zarina.domain.common.exception.ValidationException
import javax.inject.Inject

class ChangePhoneNumberApiExceptionConverter @Inject constructor(
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun handle(e: ResponseException): Nothing {
        val responseText = e.response.bodyAsText()
        val element = json.parseToJsonElement(responseText)
        when (element) {
            is JsonArray -> handleJsonArray(element, e)
            else -> throw e
        }
    }

    private fun handleJsonArray(element: JsonArray, originalException: Exception): Nothing {
        val validationErrorDtos = json.decodeFromJsonElement(
            deserializer = ListSerializer(ChangePhoneNumberErrorDto.serializer()),
            element = element,
        )
        val validationExceptions = validationErrorDtos.map { it.toValidationException() }
        val validationException = ValidationException.from(validationExceptions)
        if (validationException != null) {
            throw validationException
        } else {
            throw originalException
        }
    }
}
