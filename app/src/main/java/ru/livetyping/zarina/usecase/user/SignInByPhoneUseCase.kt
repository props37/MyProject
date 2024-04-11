package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.recaptcha.RecaptchaManager
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import timber.log.Timber
import javax.inject.Inject

class SignInByPhoneUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val recaptchaManager: RecaptchaManager,
) : UseCase<SignInByPhoneUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Sign in by phone. Phone: $phone")

        val phoneValidationException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()

        val validationException = ValidationException.from(phoneValidationException)
        if (validationException != null) throw validationException

        val recaptchaToken = recaptchaManager.execute(RecaptchaManager.ACTION_SIGN_IN_BY_PHONE)

        userRepository.signIn(phone, recaptchaToken)
    }

    data class Params(val phone: PhoneNumber)
}
