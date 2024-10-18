package ru.livetyping.zarina.data.auth.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.network.auth.markAsRefreshTokenRequest
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiQualifier
import ru.livetyping.zarina.data.auth.impl.remote.api.dto.BearerTokensDto
import javax.inject.Inject

internal class AuthApiImpl @Inject constructor(
    @ZarinaApiQualifier(ZarinaApi.UNAUTHORIZED)
    private val httpClient: HttpClient,
) : AuthApi {
    override suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokensDto {
        return httpClient.get("/api/auth/jwt") {
            header(HEADER_REFRESH_TOKEN, oldTokens.refreshToken.value)
            markAsRefreshTokenRequest()
        }.body()
    }

    private companion object {
        private const val HEADER_REFRESH_TOKEN = "x-refresh-token"
    }
}
