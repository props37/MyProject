package ru.livetyping.zarina.data.auth.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.common.Token
import ru.livetyping.zarina.core.network.util.checkNotNull

@Serializable
internal data class BearerTokensDto(
    @SerialName("token")
    val token: String? = null,

    @SerialName("refresh_token")
    val refreshToken: String? = null,
) {
    fun toBearerTokens(): BearerTokens {
        val accessToken = checkNotNull(token) { ::token }
        val refreshToken = checkNotNull(refreshToken) { ::refreshToken }
        return BearerTokens(
            accessToken = Token(accessToken),
            refreshToken = Token(refreshToken),
        )
    }
}
