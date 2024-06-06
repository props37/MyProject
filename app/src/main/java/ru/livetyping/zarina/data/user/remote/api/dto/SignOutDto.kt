package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.authorization.remote.api.dto.AuthorizationTokensDto
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens

@Serializable
data class SignOutDto(
    @SerialName("jwt")
    val tokens: AuthorizationTokensDto? = null,
) {
    fun toAuthorizationTokens(): AuthorizationTokens {
        checkNotNull(tokens) { "tokens is null" }
        return tokens.toAuthorizationTokens()
    }
}
