package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.RequestSignInByEmailConfirmationUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RequestSignInByEmailConfirmationUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RequestSignInByEmailConfirmationUseCase {

    override suspend fun execute(params: Params) {
        userRepository.requestSignInByEmailConfirmation(params.phone, params.yandexCaptchaToken)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "RequestSignInByEmailConfirmationUseCaseImpl"
    }
}
