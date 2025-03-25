package ru.livetyping.zarina.data.order.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.checkout.exception.CartChangedException
import javax.inject.Inject

class OrderCreationExceptionConverter @Inject constructor(
    private val json: Json,
) : KtorApiExceptionConverter() {
    override suspend fun handle(e: ResponseException): Nothing {
        val responseText = e.response.bodyAsText()
        val jsonElement = json.parseToJsonElement(responseText)
        when (e.response.status) {
            HttpStatusCode.BadRequest -> handleBadRequest(jsonElement, e)
            else -> throw e
        }
    }

    private fun handleBadRequest(jsonElement: JsonElement, originalException: Exception): Nothing {
        val message = jsonElement.jsonObject["message"]?.jsonPrimitive?.content
        if (message == MESSAGE_CART_CHANGED) {
            throw CartChangedException()
        } else {
            throw originalException
        }
    }

    private companion object {
        private const val MESSAGE_CART_CHANGED = "Состав корзины изменился"
    }
}
