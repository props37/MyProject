package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.BearerTokensDto

@Serializable
internal data class AuthDto(
    @SerialName("user")
    val user: UserDto? = null,

    @SerialName("jwt")
    val jwt: BearerTokensDto? = null,
) {
    fun toAuthorizationResult(): AuthResult {
        checkPropertyNotNull(jwt) { ::jwt }
        checkPropertyNotNull(user) { ::user }
        return AuthResult(
            tokens = jwt.toBearerTokens(),
            user = user.toUser(),
        )
    }
}
