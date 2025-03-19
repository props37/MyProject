package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ConfirmSignUpUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val phone: PhoneNumber,
        val otp: String,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            mindboxRepository: MindboxRepository,
            logger: UseCaseLogger?,
        ): ConfirmSignUpUseCase {
            return ConfirmSignUpUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                mindboxRepository = mindboxRepository,
                logger = logger,
            )
        }
    }
}
