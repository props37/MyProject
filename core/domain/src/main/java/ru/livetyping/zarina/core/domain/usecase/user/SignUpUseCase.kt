package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import java.time.LocalDate

public interface SignUpUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val name: String,
        val birthDate: LocalDate?,
        val email: Email,
        val phone: PhoneNumber,
        val password: String,
        val receiveEmails: Boolean,
        val receiveSms: Boolean,
        val yandexCaptchaToken: YandexCaptchaToken,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): SignUpUseCase {
            return SignUpUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
