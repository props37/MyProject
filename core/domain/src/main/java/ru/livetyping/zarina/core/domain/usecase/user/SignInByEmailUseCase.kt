package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SignInByEmailUseCase {
    public suspend operator fun invoke(params: Params): Result<AuthResult>

    public data class Params(
        val email: Email,
        val password: String,
        val yandexCaptchaToken: YandexCaptchaToken,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            mindboxRepository: MindboxRepository,
            logger: UseCaseLogger?,
        ): SignInByEmailUseCase {
            return SignInByEmailUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                mindboxRepository = mindboxRepository,
                logger = logger,
            )
        }
    }
}
