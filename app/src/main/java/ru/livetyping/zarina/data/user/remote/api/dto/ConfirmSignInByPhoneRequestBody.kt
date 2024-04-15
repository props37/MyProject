package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmSignInByPhoneRequestBody(
    @SerialName("phone")
    val phone: String,

    @SerialName("code")
    val code: String,
)
