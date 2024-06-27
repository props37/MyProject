package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePhoneNumberRequestBody(
    @SerialName("phone")
    val phone: String,

    @SerialName("reCaptchaKey")
    val recaptchaToken: String,
)
