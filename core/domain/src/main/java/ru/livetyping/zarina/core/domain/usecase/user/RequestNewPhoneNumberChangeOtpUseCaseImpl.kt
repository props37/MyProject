package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewPhoneNumberChangeOtpUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RequestNewPhoneNumberChangeOtpUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RequestNewPhoneNumberChangeOtpUseCase {

    override suspend fun execute(params: Params) {
        userRepository.requestNewPhoneNumberChangeOtp(params.phone)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "RequestNewPhoneNumberChangeOtpUseCaseImpl"
    }
}
