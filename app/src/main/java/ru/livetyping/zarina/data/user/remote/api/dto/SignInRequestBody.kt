package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SignInRequestBody {
    
    @Serializable
    data class Email(
        @SerialName("email")
        val email: String,

        @SerialName("password")
        val password: String,

        @SerialName("smartCaptchaToken")
        val yandexCaptchaToken: String,
    ) : SignInRequestBody()

    @Serializable
    data class Phone(
        @SerialName("phone")
        val phone: String,

        @SerialName("smartCaptchaToken")
        val yandexCaptchaToken: String,
    ) : SignInRequestBody()
}
