package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.impl.UserManager
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase.Params
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SignInByEmailUseCaseImpl(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val mindboxRepository: MindboxRepository,
    private val logger: UseCaseLogger?,
) : UseCase<Params, AuthResult>(logger), SignInByEmailUseCase {

    override suspend fun execute(params: Params): AuthResult {
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

        if (!authResult.isPhoneConfirmationNeeded()) {
            mindboxRepository.onUserSignedIn(user)
            val userManager = UserManager(authRepository, userRepository)
            userManager.setUserWithTokens(user, tokens)
        } else {
            logger?.v(TAG, "Phone confirmation needed")
        }

        return authResult
    }

    private fun validateFields(email: Email, password: String) {
        val validator = SignInValidator()
        val singInParams = SignInValidator.SignInByEmailParams(email, password)
        validator.validate(singInParams)
    }

    override suspend fun invoke(params: Params): Result<AuthResult> {
        return call(params)
    }

    private companion object {
        private const val TAG = "SignInByEmailUseCaseImpl"
    }
}
