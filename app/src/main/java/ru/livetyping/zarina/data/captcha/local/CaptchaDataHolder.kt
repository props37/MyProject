package ru.livetyping.zarina.data.captcha.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaptchaDataHolder @Inject constructor() {
    private val yandexCaptcha = MutableStateFlow<YandexCaptcha?>(null)

    fun getYandexCaptcha(): Flow<YandexCaptcha?> {
        return yandexCaptcha
    }

    fun setYandexCaptcha(captcha: YandexCaptcha) {
        yandexCaptcha.value = captcha
    }
}
