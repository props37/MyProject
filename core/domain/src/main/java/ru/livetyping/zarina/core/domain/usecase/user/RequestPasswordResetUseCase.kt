package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RequestPasswordResetUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val email: Email)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): RequestPasswordResetUseCase {
            return RequestPasswordResetUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
