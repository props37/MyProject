package ru.livetyping.zarina.data.user.impl.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.livetyping.zarina.core.domain.model.user.exception.UserNotFoundException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import ru.livetyping.zarina.core.network.di.NetworkJson
import javax.inject.Inject

internal class SignInApiExceptionConverter @Inject constructor(
    @NetworkJson
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun handle(e: ClientRequestException): Nothing {
        val responseText = e.response.bodyAsText()
        val element = json.parseToJsonElement(responseText)
        when (element) {
            is JsonObject -> handleJsonObject(element, e)
            else -> throw e
        }
    }

    private fun handleJsonObject(element: JsonObject, originalException: Exception): Nothing {
        val message = element["message"]
        if (message is JsonPrimitive) {
            when (message.content) {
                MESSAGE_INVALID_CODE, MESSAGE_USER_NOT_FOUND -> throw UserNotFoundException()
                else -> throw originalException
            }
        } else {
            throw originalException
        }
    }

    companion object {
        private const val MESSAGE_INVALID_CODE = "Неверный email или пароль"
        private const val MESSAGE_USER_NOT_FOUND = "Пользователь с таким номером телефона не зарегистрирован"
    }
}
