package ru.livetyping.zarina.util.library.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

fun HttpClient.clearBearerTokens() {
    this.authProvider<BearerAuthProvider>()?.clearToken()
}
