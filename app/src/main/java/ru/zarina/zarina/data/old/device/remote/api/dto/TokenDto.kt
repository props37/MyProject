package ru.zarina.zarina.data.old.device.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.AuthorizationToken

@Serializable
data class TokenDto(
    @SerialName("token")
    val token: String? = null,
) {
    fun toDomain() = AuthorizationToken.Device(checkNotNull(token))
}
