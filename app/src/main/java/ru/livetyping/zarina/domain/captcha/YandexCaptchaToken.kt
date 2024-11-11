package ru.livetyping.zarina.domain.captcha

data class YandexCaptchaToken(
    val token: String,
    val mode: YandexCaptchaMode,
)
