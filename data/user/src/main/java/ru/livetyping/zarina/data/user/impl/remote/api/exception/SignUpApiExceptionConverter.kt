package ru.livetyping.zarina.data.user.impl.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailAlreadyUsedException
import ru.livetyping.zarina.core.domain.model.user.exception.OtpTimeoutException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignUpErrorDtoSerializer
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignUpFieldValidationErrorDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.SignUpMessageErrorDto
import javax.inject.Inject

internal class SignUpApiExceptionConverter @Inject constructor(
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun handle(e: ClientRequestException): Nothing {
        val responseText = e.response.bodyAsText()
        val element = json.parseToJsonElement(responseText)
        when (element) {
            is JsonArray -> handleJsonArray(element, e)
            is JsonObject -> handleJsonObject(element, e)
            is JsonPrimitive -> throw e
            JsonNull -> throw e
        }
    }

    private fun handleJsonArray(element: JsonArray, originalException: Exception): Nothing {
        val validationErrorDtos = json.decodeFromJsonElement(
            deserializer = ListSerializer(SignUpFieldValidationErrorDto.serializer()),
            element = element,
        )
        val validationExceptions = validationErrorDtos.map { it.toException() }
        throw when {
            validationExceptions.size == 1 -> validationExceptions.first()
            validationExceptions.size > 1 -> CombinedValidationException(validationExceptions)
            else -> originalException
        }
    }

    private fun handleJsonObject(element: JsonObject, originalException: Exception): Nothing {
        val errorDto = json.decodeFromJsonElement(SignUpErrorDtoSerializer(), element)
        when (errorDto) {
            is SignUpFieldValidationErrorDto -> throw errorDto.toException()
            is SignUpMessageErrorDto -> {
                val message = checkNotNull(errorDto.message) { "message is null" }
                when (message) {
                    MESSAGE_EMAIL_ALREADY_IN_USE -> throw EmailAlreadyUsedException()
                    MESSAGE_OTP_TIMEOUT -> throw OtpTimeoutException()
                    else -> throw originalException
                }
            }
        }
    }

    companion object {
        private const val MESSAGE_EMAIL_ALREADY_IN_USE =
            "Пользователь с таким Email уже зарегистрирован"
        private const val MESSAGE_OTP_TIMEOUT =
            "Запрос на повторную отправку смс сделан слишком рано"
    }
}
