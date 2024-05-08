package ru.livetyping.zarina.data.common.remote.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.pluginOrNull
import ru.livetyping.zarina.data.common.remote.ktor.plugin.ZarinaAuth
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class HttpClientAuthorizationTokensCleaner @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    fun clearAuthorizationTokens() {
        Timber.v("Clear HttpClient authorization tokens")
        httpClient.clearBearerTokens()
    }
}

private fun HttpClient.clearBearerTokens() {
    this.pluginOrNull(Auth)
        ?.providers
        ?.filterIsInstance<BearerAuthProvider>()
        ?.firstOrNull()
        ?.clearToken()
    this.pluginOrNull(ZarinaAuth)
        ?.providers
        ?.filterIsInstance<BearerAuthProvider>()
        ?.firstOrNull()
        ?.clearToken()
}
