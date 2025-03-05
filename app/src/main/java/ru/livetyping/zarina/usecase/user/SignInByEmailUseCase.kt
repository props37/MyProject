package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaSignInMethod
import ru.livetyping.zarina.data.mindbox.MindboxApi
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.authorization.AuthorizationResult
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import timber.log.Timber
import javax.inject.Inject

class SignInByEmailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateFieldsUseCase: ValidateSignInByEmailFieldsUseCase,
    private val setUserWithAuthorizationTokensUseCase: SetUserWithAuthorizationTokensUseCase,
    private val mindboxApi: MindboxApi,
) : UseCase<SignInByEmailUseCase.Params, AuthorizationResult>() {

    override suspend fun execute(params: Params): AuthorizationResult {
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

        if (!authorizationResult.isPhoneConfirmationNeeded()) {
            mindboxApi.userSignedIn(user)
            val setUserWithTokensParams = SetUserWithAuthorizationTokensUseCase.Params(user, tokens)
            setUserWithAuthorizationTokensUseCase(setUserWithTokensParams).getOrThrow()
            AppMetricaHelper.reportUserSignedIn(AppMetricaSignInMethod.PASSWORD)
        } else {
            Timber.v("Phone confirmation needed")
        }

        return authorizationResult
    }

    data class Params(
        val email: Email,
        val password: String,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
