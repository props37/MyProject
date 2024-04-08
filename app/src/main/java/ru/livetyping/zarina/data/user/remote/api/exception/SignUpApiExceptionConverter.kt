package ru.livetyping.zarina.data.user.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpErrorDtoSerializer
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpFieldValidationErrorDto
import ru.livetyping.zarina.data.user.remote.api.dto.SignUpMessageErrorDto
import ru.livetyping.zarina.domain.common.exception.OtpTimeoutException
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.EmailAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.InvalidCaptchaException
import javax.inject.Inject

class SignUpApiExceptionConverter @Inject constructor(
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
        val validationExceptions = validationErrorDtos.map { it.toValidationException() }
        val validationException = ValidationException.from(validationExceptions)
        if (validationException != null) {
            throw validationException
        } else {
            throw originalException
        }
    }

    private fun handleJsonObject(element: JsonObject, originalException: Exception): Nothing {
        val errorDto = json.decodeFromJsonElement(SignUpErrorDtoSerializer(), element)
        when (errorDto) {
            is SignUpFieldValidationErrorDto -> throw errorDto.toValidationException()
            is SignUpMessageErrorDto -> {
                val message = checkNotNull(errorDto.message) { "message is null" }
                when (message) {
                    MESSAGE_INVALID_RECAPTCHA -> {
                        throw InvalidCaptchaException()
                    }

                    MESSAGE_EMAIL_ALREADY_IN_USE -> {
                        throw EmailAlreadyInUseException()
                    }

                    MESSAGE_OTP_TIMEOUT -> throw OtpTimeoutException()
                    else -> throw originalException
                }
            }
        }
    }

    companion object {
        private const val MESSAGE_INVALID_RECAPTCHA = "Recaptcha not valid"
        private const val MESSAGE_EMAIL_ALREADY_IN_USE =
            "Пользователь с таким Email уже зарегистрирован"
        private const val MESSAGE_OTP_TIMEOUT =
            "Запрос на повторную отправку смс сделан слишком рано"
    }
}
