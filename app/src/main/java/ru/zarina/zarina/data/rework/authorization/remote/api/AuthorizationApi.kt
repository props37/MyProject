package ru.zarina.zarina.data.rework.authorization.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import ru.zarina.zarina.data.rework.authorization.remote.api.dto.AuthorizationTokensDto
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class AuthorizationApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.UNAUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getUnauthorizedUserAuthorizationTokens(): AuthorizationTokensDto {
        return httpClient.get("/api/auth/jwt").body()
    }

    suspend fun refreshAuthorizationTokens(tokens: AuthorizationTokens): AuthorizationTokensDto {
        val body = RefreshAuthorizationTokensBody(tokens.refreshToken.value)
        return httpClient.get("/api/auth/jwt") {
            setJsonBody(body)
            markAsRefreshTokenRequest()
        }.body()
    }

    private fun HttpRequestBuilder.markAsRefreshTokenRequest() {
        attributes.put(Auth.AuthCircuitBreaker, Unit)
    }

    private data class RefreshAuthorizationTokensBody(
        @SerialName("refresh_token")
        val refreshToken: String,
    )
}
