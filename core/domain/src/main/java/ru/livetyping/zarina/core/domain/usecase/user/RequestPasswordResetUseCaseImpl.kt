package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.RequestPasswordResetUseCase.Params
import ru.livetyping.zarina.core.domain.validation.EmailValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RequestPasswordResetUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RequestPasswordResetUseCase {

    override suspend fun execute(params: Params) {
        val email = params.email

        EmailValidator().validate(email)

        userRepository.requestPasswordReset(email)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "RequestPasswordResetUseCaseImpl"
    }
}
