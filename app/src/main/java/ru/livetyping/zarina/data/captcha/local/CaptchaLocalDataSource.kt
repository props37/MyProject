package ru.livetyping.zarina.data.captcha.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class CaptchaLocalDataSource @Inject constructor(
    private val dataHolder: CaptchaDataHolder,
) {
    fun getYandexCaptcha(): Flow<YandexCaptcha?> {
        return dataHolder.getYandexCaptcha()
    }

    fun setYandexCaptcha(captcha: YandexCaptcha) {
        dataHolder.setYandexCaptcha(captcha)
    }
}
