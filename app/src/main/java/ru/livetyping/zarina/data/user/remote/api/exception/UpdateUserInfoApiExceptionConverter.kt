package ru.livetyping.zarina.data.user.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.user.exception.InvalidFirstNameException
import ru.livetyping.zarina.domain.user.exception.InvalidLastNameException
import ru.livetyping.zarina.domain.user.exception.InvalidOldPasswordException
import javax.inject.Inject

class UpdateUserInfoApiExceptionConverter @Inject constructor(
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun handle(e: ResponseException): Nothing {
        val responseText = e.response.bodyAsText()
        val element = json.parseToJsonElement(responseText)
        when (element) {
            is JsonObject -> handleJsonObject(element, e)
            is JsonArray -> handleJsonArray(element, e)
            else -> throw e
        }
    }

    private fun handleJsonObject(element: JsonObject, originalException: Exception): Nothing {
        val message = element["message"]
        if (message is JsonPrimitive) {
            when (message.content) {
                MESSAGE_INVALID_OLD_PASSWORD -> throw InvalidOldPasswordException()
                else -> throw originalException
            }
        } else {
            throw originalException
        }
    }

    private fun handleJsonArray(element: JsonArray, originalException: Exception): Nothing {
        when (val firstElement = element.elementAtOrNull(0)) {
            is JsonObject -> {
                val fieldName = firstElement["field_name"]?.jsonPrimitive?.content
                val description = firstElement["description"]?.jsonPrimitive?.content
                when (fieldName) {
                    FIELD_NAME_FIRST_NAME -> {
                        throw InvalidFirstNameException(localizedMessage = description)
                    }

                    FIELD_NAME_LAST_NAME -> {
                        throw InvalidLastNameException(localizedMessage = description)
                    }

                    else -> throw originalException
                }
            }

            else -> throw originalException
        }
    }

    companion object {
        private const val MESSAGE_INVALID_OLD_PASSWORD = "Неверный пароль"

        private const val FIELD_NAME_FIRST_NAME = "first_name"
        private const val FIELD_NAME_LAST_NAME = "last_name"
    }
}
