package ru.livetyping.zarina.usecase.user

import com.google.android.recaptcha.RecaptchaAction
import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.recaptcha.RecaptchaManager
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.exception.ValidationException
import timber.log.Timber
import javax.inject.Inject

class SignInByEmailUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val recaptchaManager: RecaptchaManager,
) : UseCase<SignInByEmailUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val email = params.email
        val password = params.password
        Timber.v("Sign in by email. Email: $email, password: $password")

        val emailValidationException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()
        val passwordValidationException =
            validatePasswordUseCase(ValidatePasswordUseCase.Params(password)).exceptionOrNull()

        val validationException = ValidationException.from(
            emailValidationException,
            passwordValidationException,
        )
        if (validationException != null) throw validationException

        val recaptchaToken = recaptchaManager.execute(RecaptchaAction.LOGIN)

        userRepository.signIn(email, password, recaptchaToken)
    }

    data class Params(
        val email: Email,
        val password: String,
    )
}
