package ru.livetyping.zarina.usecase.captcha

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.captcha.CaptchaRepository
import ru.livetyping.zarina.domain.captcha.YandexCaptcha
import javax.inject.Inject

class GetYandexCaptchaFlowUseCase @Inject constructor(
    private val captchaRepository: CaptchaRepository,
) : FlowUseCase<Unit, YandexCaptcha?>() {
    override fun execute(params: Unit): Flow<YandexCaptcha?> {
        return captchaRepository.getYandexCaptchaUrl()
    }
}
