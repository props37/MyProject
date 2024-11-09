package ru.livetyping.zarina.core.network.auth

import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.HttpRequestBuilder

public fun HttpRequestBuilder.markAsRefreshTokenRequest() {
    attributes.put(AuthCircuitBreaker, Unit)
}
