package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import timber.log.Timber
import javax.inject.Inject

class SignInByEmailUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val validateFieldsUseCase: ValidateSignInByEmailFieldsUseCase,
    private val setUserWithAuthorizationTokensUseCase: SetUserWithAuthorizationTokensUseCase,
) : UseCase<SignInByEmailUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val email = params.email
        val password = params.password
        Timber.v("Sign in by email. Email: $email, password: $password")

        val validationParams = ValidateSignInByEmailFieldsUseCase.Params(email, password)
        validateFieldsUseCase(validationParams).getOrThrow()

        val authorizationResult = userRepository.signIn(
            email = email,
            password = password,
            yandexCaptchaToken = params.yandexCaptchaToken,
        )
        val tokens = authorizationResult.tokens
        val user = authorizationResult.user

        val setUserWithTokensParams = SetUserWithAuthorizationTokensUseCase.Params(user, tokens)
        setUserWithAuthorizationTokensUseCase(setUserWithTokensParams).getOrThrow()
    }

    data class Params(
        val email: Email,
        val password: String,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
