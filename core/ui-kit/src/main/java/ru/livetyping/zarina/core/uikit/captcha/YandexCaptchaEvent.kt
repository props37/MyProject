package ru.livetyping.zarina.core.uikit.captcha

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken

public sealed interface YandexCaptchaEvent {
    public data class DismissRequested(val reason: YandexCaptchaReason?) : YandexCaptchaEvent

    public data class TokenReceived(
        val token: YandexCaptchaToken,
        val reason: YandexCaptchaReason?,
    ) : YandexCaptchaEvent
}
