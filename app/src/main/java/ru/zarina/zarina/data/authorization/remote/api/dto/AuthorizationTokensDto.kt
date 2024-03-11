package ru.zarina.zarina.data.authorization.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.domain.rework.common.Token

@Serializable
data class AuthorizationTokensDto(
    @SerialName("token") 
    val accessToken: String? = null,
    
    @SerialName("refresh_token")
    val refreshToken: String? = null,
) {
    fun toAuthorizationTokens(): AuthorizationTokens {
        checkNotNull(accessToken) { "accessToken is null" }
        checkNotNull(refreshToken) { "refreshToken is null" }
        return AuthorizationTokens(
            accessToken = Token(accessToken),
            refreshToken = Token(refreshToken),
        )
    }
}
