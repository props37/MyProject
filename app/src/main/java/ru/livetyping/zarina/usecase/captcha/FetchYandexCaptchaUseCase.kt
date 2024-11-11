package ru.livetyping.zarina.usecase.captcha

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.captcha.CaptchaRepository
import javax.inject.Inject

class FetchYandexCaptchaUseCase @Inject constructor(
    private val captchaRepository: CaptchaRepository,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        captchaRepository.fetchYandexCaptchaUrl()
    }
}
