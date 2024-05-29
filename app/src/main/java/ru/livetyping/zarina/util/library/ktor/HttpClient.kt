package ru.livetyping.zarina.util.library.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.pluginOrNull

fun HttpClient.clearBearerTokens() {
    this.pluginOrNull(Auth)
        ?.providers
        ?.filterIsInstance<BearerAuthProvider>()
        ?.firstOrNull()
        ?.clearToken()
}
