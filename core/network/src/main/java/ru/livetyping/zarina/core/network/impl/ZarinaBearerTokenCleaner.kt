package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import ru.livetyping.zarina.core.network.auth.BearerTokenCleaner
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.clearBearerTokens
import javax.inject.Inject

internal class ZarinaBearerTokenCleaner @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : BearerTokenCleaner {
    override fun clearBearerTokens() {
        httpClient.clearBearerTokens()
    }
}
