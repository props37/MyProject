package ru.livetyping.zarina.data.common.remote.ktor

import io.ktor.client.HttpClient
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.util.library.ktor.clearBearerTokens
import timber.log.Timber
import javax.inject.Inject

class HttpClientAuthorizationTokensCleaner @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    fun clearAuthorizationTokens() {
        Timber.v("Clear HttpClient authorization tokens")
        httpClient.clearBearerTokens()
    }
}
