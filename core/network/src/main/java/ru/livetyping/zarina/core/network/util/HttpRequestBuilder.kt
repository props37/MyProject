package ru.livetyping.zarina.core.network.util

import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public inline fun <reified T> HttpRequestBuilder.setJsonBody(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}

public fun HttpRequestBuilder.markAsRefreshTokenRequest() {
    attributes.put(AuthCircuitBreaker, Unit)
}
