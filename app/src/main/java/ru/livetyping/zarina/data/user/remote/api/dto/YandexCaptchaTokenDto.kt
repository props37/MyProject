package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.captcha.YandexCaptchaMode
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken

@Serializable
@JvmInline
value class YandexCaptchaTokenDto private constructor(val value: String) {
    companion object {
        fun from(token: YandexCaptchaToken): YandexCaptchaTokenDto {
            val value = when (token.mode) {
                YandexCaptchaMode.SLIDER -> token.token
                YandexCaptchaMode.CHECKBOX -> "$CHECKBOX_MODE_TOKEN_PREFIX${token.token}"
            }
            return YandexCaptchaTokenDto(value)
        }

        private const val CHECKBOX_MODE_TOKEN_PREFIX = "checkbox_"
    }
}
