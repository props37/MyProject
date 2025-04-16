package ru.livetyping.zarina.data.checkout.impl.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.livetyping.zarina.core.domain.model.giftcert.exception.GiftCertificateReservedException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import ru.livetyping.zarina.core.network.di.NetworkJson
import javax.inject.Inject

internal class ApplyGiftCertificateApiExceptionConverter @Inject constructor(
    @NetworkJson
    private val json: Json,
) : KtorApiExceptionConverter() {
    override suspend fun convert(e: ResponseException): Nothing {
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