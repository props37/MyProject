package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.BearerTokensDto

@Serializable
internal data class SignOutDto(
    @SerialName("jwt")
    val jwt: BearerTokensDto? = null,
) {
    fun toBearerTokens(): BearerTokens {
        checkPropertyNotNull(jwt) { ::jwt }
        return jwt.toBearerTokens()
    }
}
