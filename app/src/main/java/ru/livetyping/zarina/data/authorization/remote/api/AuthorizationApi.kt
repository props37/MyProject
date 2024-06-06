package ru.livetyping.zarina.data.authorization.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import ru.livetyping.zarina.data.authorization.remote.api.dto.AuthorizationTokensDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.UNAUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getUnauthorizedUserAuthorizationTokens(): AuthorizationTokensDto {
        return httpClient.get("/api/auth/jwt").body()
    }

    suspend fun refreshAuthorizationTokens(tokens: AuthorizationTokens): AuthorizationTokensDto {
        return httpClient.get("/api/auth/jwt") {
            header(HEADER_REFRESH_TOKEN, tokens.refreshToken.value)
            markAsRefreshTokenRequest()
        }.body()
    }

    private fun HttpRequestBuilder.markAsRefreshTokenRequest() {
        attributes.put(Auth.AuthCircuitBreaker, Unit)
    }

    companion object {
        private const val HEADER_REFRESH_TOKEN = "x-refresh-token"
    }
}
