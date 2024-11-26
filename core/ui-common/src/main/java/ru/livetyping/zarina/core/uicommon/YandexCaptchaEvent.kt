package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken

public sealed interface YandexCaptchaEvent {
    public data object DismissRequested : YandexCaptchaEvent

    public data class TokenReceived(val token: YandexCaptchaToken) : YandexCaptchaEvent
}
