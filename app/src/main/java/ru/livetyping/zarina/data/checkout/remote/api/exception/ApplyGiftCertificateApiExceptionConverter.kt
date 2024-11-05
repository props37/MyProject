package ru.livetyping.zarina.data.checkout.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.checkout.exception.GiftCertificateReservedException
import javax.inject.Inject

class ApplyGiftCertificateApiExceptionConverter @Inject constructor(
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
            val regex = CERTIFICATE_RESERVED_MESSAGE_REGEX_PATTERN.toRegex()
            if (regex.containsMatchIn(message.content)) {
                throw GiftCertificateReservedException()
            } else {
                throw originalException
            }
        } else {
            throw originalException
        }
    }

    companion object {
        private const val CERTIFICATE_RESERVED_MESSAGE_REGEX_PATTERN =
            "Сумма \\d+ заблокирована другим заказом"
    }
}
