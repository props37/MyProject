package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.authorization.remote.api.dto.AuthorizationTokensDto
import ru.livetyping.zarina.data.common.remote.api.dto.UserDto
import ru.livetyping.zarina.domain.authorization.AuthorizationResult

@Serializable
data class AuthorizationDto(
    @SerialName("user")
    val user: UserDto? = null,

    @SerialName("jwt")
    val authorizationTokens: AuthorizationTokensDto? = null,
) {
    fun toAuthorizationResult(): AuthorizationResult {
        checkNotNull(authorizationTokens) { "authorizationTokens is null" }
        checkNotNull(user) { "user is null" }
        return AuthorizationResult(
            tokens = authorizationTokens.toAuthorizationTokens(),
            user = user.toUser(),
        )
    }
}
