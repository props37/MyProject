package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.impl.UserWithBearerTokensSetter
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInByEmailUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ConfirmSignInByEmailUseCaseImpl(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ConfirmSignInByEmailUseCase {

    override suspend fun execute(params: Params) {
        val authResult = userRepository.confirmSignInByEmail(params.phone, params.otp)
        val user = authResult.user
        val tokens = authResult.tokens

        val userWithTokensSetter = UserWithBearerTokensSetter(authRepository, userRepository)
        userWithTokensSetter.set(user, tokens)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ConfirmSignInByEmailUseCaseImpl"
    }
}
