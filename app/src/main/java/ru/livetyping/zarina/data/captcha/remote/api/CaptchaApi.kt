package ru.livetyping.zarina.data.captcha.remote.api

import io.ktor.client.HttpClient
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import ru.livetyping.zarina.domain.common.Url
import javax.inject.Inject

class CaptchaApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getYandexCaptcha(): YandexCaptcha {
        // TODO: [High] Implement
        return YandexCaptcha(
            url = Url("https://smartcaptcha.yandexcloud.net/webview"),
            isInvisible = true,
        )
    }
}
