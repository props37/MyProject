package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.impl.UserWithBearerTokensSetter
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInByPhoneUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ConfirmSignInByPhoneUseCaseImpl(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ConfirmSignInByPhoneUseCase {

    override suspend fun execute(params: Params) {
        val authResult = userRepository.confirmSignInByPhone(params.phone, params.otp)

        val userWithTokensSetter = UserWithBearerTokensSetter(
            authRepository = authRepository,
            userRepository = userRepository,
        )
        userWithTokensSetter.set(authResult.user, authResult.tokens)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ConfirmSignInByPhoneUseCaseImpl"
    }
}
