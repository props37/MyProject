package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetYandexCaptchaUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, YandexCaptcha>(logger), GetYandexCaptchaUseCase {

    override suspend fun execute(params: Unit): YandexCaptcha {
        return userRepository.getYandexCaptcha()
    }

    override suspend fun invoke(): Result<YandexCaptcha> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetYandexCaptchaUseCaseImpl"
    }
}
