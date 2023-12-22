package ru.zarina.zarina.data.authorization.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.authorization.remote.api.dto.AuthorizationTokensDto
import ru.zarina.zarina.di.reworked.Qualifiers
import javax.inject.Inject

class AuthorizationApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.UNAUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getUnauthorizedUserAuthorizationTokens(): AuthorizationTokensDto {
        return httpClient.get("/api/auth/jwt").body()
    }
}
