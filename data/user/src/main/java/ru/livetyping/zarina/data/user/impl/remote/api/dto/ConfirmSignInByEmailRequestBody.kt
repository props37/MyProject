package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConfirmSignInByEmailRequestBody(
    @SerialName("phone")
    val phone: String,

    @SerialName("code")
    val code: String,
)
