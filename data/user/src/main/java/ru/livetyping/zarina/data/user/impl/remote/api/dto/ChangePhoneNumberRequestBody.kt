package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChangePhoneNumberRequestBody(
    @SerialName("phone")
    val phone: String,

    @SerialName("smartCaptchaToken")
    val yandexCaptchaToken: String,
)
