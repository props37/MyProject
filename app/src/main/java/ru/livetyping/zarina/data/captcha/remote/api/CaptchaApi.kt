package ru.livetyping.zarina.data.captcha.remote.api

import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import ru.livetyping.zarina.domain.common.Url
import javax.inject.Inject

class CaptchaApi @Inject constructor() {
    fun getYandexCaptcha(): YandexCaptcha {
        return YandexCaptcha(
            url = Url("${BuildConfig.BACKEND_URL}/api/v1/smartCaptcha/"),
        )
    }
}
