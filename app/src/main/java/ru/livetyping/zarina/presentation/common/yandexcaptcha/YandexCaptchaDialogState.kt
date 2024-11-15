package ru.livetyping.zarina.presentation.common.yandexcaptcha

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.domain.captcha.YandexCaptcha

@Stable
sealed class YandexCaptchaDialogState {
    data object Hidden : YandexCaptchaDialogState()

    @Immutable
    data class Visible(val captcha: YandexCaptcha) : YandexCaptchaDialogState()
}
