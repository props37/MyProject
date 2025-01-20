package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestResendAuthSmsOtpRequestBody(
    @SerialName("phone")
    val phone: String,

    @SerialName("smartCaptchaToken")
    val yandexCaptchaToken: String,
)
