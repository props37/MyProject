package ru.livetyping.zarina.core.network.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

internal fun HttpClient.clearBearerTokens() {
    this.authProvider<BearerAuthProvider>()?.clearToken()
}
