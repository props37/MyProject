package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ConfirmPhoneNumberChangeUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val phone: PhoneNumber,
        val otp: String,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): ConfirmPhoneNumberChangeUseCase {
            return ConfirmPhoneNumberChangeUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
