package ru.livetyping.zarina.core.uikit.captcha

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha

@Stable
public sealed class YandexCaptchaState {
    @Immutable
    public data class Started(
        val yandexCaptcha: YandexCaptcha,
        val reason: YandexCaptchaReason? = null,
    ) : YandexCaptchaState()

    public data object None : YandexCaptchaState()
}
