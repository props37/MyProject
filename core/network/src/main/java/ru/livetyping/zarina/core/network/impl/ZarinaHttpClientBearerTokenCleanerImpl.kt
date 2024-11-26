package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import ru.livetyping.zarina.core.network.auth.ZarinaHttpClientBearerTokenCleaner
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.clearBearerTokens
import javax.inject.Inject
import javax.inject.Provider

internal class ZarinaHttpClientBearerTokenCleanerImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: Provider<HttpClient>,
) : ZarinaHttpClientBearerTokenCleaner {
    override fun clearBearerTokens() {
        httpClient.get().clearBearerTokens()
    }
}
