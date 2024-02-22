package ru.zarina.zarina.data.rework.authorization.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshAuthorizationTokensRequestBody(
    @SerialName("refresh_token")
    val refreshToken: String,
)
