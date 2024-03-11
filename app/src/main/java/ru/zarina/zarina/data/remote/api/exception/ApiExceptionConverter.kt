package ru.zarina.zarina.data.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import ru.zarina.zarina.domain.rework.user.exception.InvalidEmailException
import ru.zarina.zarina.domain.rework.user.exception.InvalidFirstNameException

suspend inline fun <T> apiExceptionConverter(block: () -> T): T {
    return try {
        block()
    } catch (e: ClientRequestException) {
        if (e.response.status == HttpStatusCode.UnprocessableEntity) {
            val responseText = e.response.bodyAsText()
            when {
                responseText.contains("email") -> throw InvalidEmailException()
                responseText.contains("first_name") -> throw InvalidFirstNameException()
                else -> throw e
            }
        } else {
            throw e
        }
    }
}
