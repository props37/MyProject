package ru.livetyping.zarina.data.auth.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.markAsRefreshTokenRequest
import ru.livetyping.zarina.core.network.zarina.dto.BearerTokensDto
import javax.inject.Inject

internal class AuthApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.UNAUTHORIZED)
    private val httpClient: HttpClient,
) : AuthApi {
    override suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokensDto {
        return httpClient.get("/api/auth/jwt") {
            header(HEADER_REFRESH_TOKEN, oldTokens.refreshToken.value)
            markAsRefreshTokenRequest()
        }.body()
    }

    override suspend fun getNewUnauthorizedUserAuthorizationTokens(): BearerTokensDto {
        return httpClient.get("/api/auth/jwt").body()
    }

    private companion object {
        private const val HEADER_REFRESH_TOKEN = "x-refresh-token"
    }
}
