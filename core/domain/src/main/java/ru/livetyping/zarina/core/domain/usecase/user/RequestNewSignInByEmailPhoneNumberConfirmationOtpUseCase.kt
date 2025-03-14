package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RequestNewSignInByEmailPhoneNumberConfirmationOtpUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val phone: PhoneNumber)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): RequestNewSignInByEmailPhoneNumberConfirmationOtpUseCase {
            return RequestNewSignInByEmailPhoneNumberConfirmationOtpUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
