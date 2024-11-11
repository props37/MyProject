package ru.livetyping.zarina.data.captcha

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.captcha.local.CaptchaLocalDataSource
import ru.livetyping.zarina.data.captcha.remote.CaptchaRemoteDataSource
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class CaptchaRepository @Inject constructor(
    private val remoteDataSource: CaptchaRemoteDataSource,
    private val localDataSource: CaptchaLocalDataSource,
) {
    fun getYandexCaptchaUrl(): Flow<YandexCaptcha?> {
        return localDataSource.getYandexCaptcha()
            .onEach { captcha ->
                if (captcha == null) fetchYandexCaptchaUrl()
            }
    }

    suspend fun fetchYandexCaptchaUrl() {
        val yandexCaptcha = remoteDataSource.getYandexCaptcha()
        localDataSource.setYandexCaptcha(yandexCaptcha)
    }
}
