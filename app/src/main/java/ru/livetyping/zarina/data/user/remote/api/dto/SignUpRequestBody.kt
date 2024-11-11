package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestBody(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("birthday")
    val birthDate: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("password")
    val password: String,

    @SerialName("subscribe_email")
    val receiveEmails: Boolean,

    @SerialName("subscribe_sms")
    val receiveSms: Boolean,

    @SerialName("smartCaptchaToken")
    val yandexCaptchaToken: YandexCaptchaTokenDto,
)
