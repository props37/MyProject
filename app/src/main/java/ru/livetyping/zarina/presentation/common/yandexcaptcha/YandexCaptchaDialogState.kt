package ru.livetyping.zarina.presentation.common.yandexcaptcha

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class YandexCaptchaDialogState {
    data object Hidden : YandexCaptchaDialogState()

    @Immutable
    data class Visible(
        val url: String,
        val isCaptchaInvisible: Boolean,
    ) : YandexCaptchaDialogState()
}
