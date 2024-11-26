package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SignInByEmailUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    // TODO: [Top] Add yandex captcha token
    public data class Params(
        val email: Email,
        val password: String,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            logger: UseCaseLogger?,
        ): SignInByEmailUseCase {
            return SignInByEmailUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                logger = logger,
            )
        }
    }
}
