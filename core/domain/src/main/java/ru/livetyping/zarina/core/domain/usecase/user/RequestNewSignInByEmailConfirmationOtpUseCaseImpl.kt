package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewSignInByEmailConfirmationOtpUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RequestNewSignInByEmailConfirmationOtpUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RequestNewSignInByEmailConfirmationOtpUseCase {

    override suspend fun execute(params: Params) {
        userRepository.requestNewSignInByEmailConfirmationOtp(params.phone)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "RequestNewSignInByEmailConfirmationOtpUseCaseImpl"
    }
}
