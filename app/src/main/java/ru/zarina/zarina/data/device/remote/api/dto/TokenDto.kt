package ru.zarina.zarina.data.device.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.AuthorizationToken

@Serializable
data class TokenDto(
    @SerialName("token")
    val token: String?,
) {
    fun toDomain() = AuthorizationToken.Device(checkNotNull(token))
}
