package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RequestSignInPhoneConfirmationUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val phone: PhoneNumber,
        val yandexCaptchaToken: YandexCaptchaToken,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): RequestSignInPhoneConfirmationUseCase {
            return RequestSignInPhoneConfirmationUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
