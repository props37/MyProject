package ru.livetyping.zarina.usecase.captcha

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.captcha.CaptchaRepository
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class GetYandexCaptchaUseCase @Inject constructor(
    private val captchaRepository: CaptchaRepository,
) : UseCase<Unit, YandexCaptcha?>() {
    override suspend fun execute(params: Unit): YandexCaptcha {
        return captchaRepository.getYandexCaptcha()
    }
}
