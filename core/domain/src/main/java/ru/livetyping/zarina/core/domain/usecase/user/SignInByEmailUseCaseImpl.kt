package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.impl.UserWithBearerTokensSetter
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.user.SignInByEmailParams
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase.Params
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SignInByEmailUseCaseImpl(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SignInByEmailUseCase {

    override suspend fun execute(params: Params) {
        val email = params.email
        val password = params.password
        val yandexCaptchaToken = params.yandexCaptchaToken

        validateFields(email, password)

        val authResult = userRepository.signIn(
            email = email,
            password = password,
            yandexCaptchaToken = yandexCaptchaToken,
        )
        val tokens = authResult.tokens
        val user = authResult.user

        val userWithBearerTokensSetter = UserWithBearerTokensSetter(
            authRepository = authRepository,
            userRepository = userRepository,
        )
        userWithBearerTokensSetter.set(user, tokens)
    }

    private fun validateFields(email: Email, password: String) {
        val validator = SignInValidator()
        val singInParams = SignInByEmailParams(email, password)
        validator.validate(singInParams)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "SignInByEmailUseCaseImpl"
    }
}
