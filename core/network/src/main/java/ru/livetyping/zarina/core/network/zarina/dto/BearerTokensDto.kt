package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.common.Token
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
public data class BearerTokensDto(
    @SerialName("token")
    val token: String? = null,

    @SerialName("refresh_token")
    val refreshToken: String? = null,
) {
    public fun toBearerTokens(): BearerTokens {
        val accessToken = checkPropertyNotNull(token) { ::token }
        val refreshToken = checkPropertyNotNull(refreshToken) { ::refreshToken }
        return BearerTokens(
            accessToken = Token(accessToken),
            refreshToken = Token(refreshToken),
        )
    }
}
