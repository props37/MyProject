package ru.livetyping.zarina.data.captcha.remote

import ru.livetyping.zarina.data.captcha.remote.api.CaptchaApi
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class CaptchaRemoteDataSource @Inject constructor(
    private val api: CaptchaApi,
) {
    suspend fun getYandexCaptcha(): YandexCaptcha {
        return api.getYandexCaptcha()
    }
}
