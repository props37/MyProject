package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetYandexCaptchaUseCase {
    public suspend operator fun invoke(): Result<YandexCaptcha>

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetYandexCaptchaUseCase {
            return GetYandexCaptchaUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
