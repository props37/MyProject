package ru.livetyping.zarina.data.captcha

import ru.livetyping.zarina.data.captcha.remote.CaptchaRemoteDataSource
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class CaptchaRepository @Inject constructor(
    private val remoteDataSource: CaptchaRemoteDataSource,
) {
    fun getYandexCaptcha(): YandexCaptcha {
        return remoteDataSource.getYandexCaptcha()
    }
}
