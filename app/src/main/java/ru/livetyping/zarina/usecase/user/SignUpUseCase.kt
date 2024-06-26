package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.recaptcha.RecaptchaManager
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.EmptyDateException
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.USER_BIRTH_DATE_DEFAULT
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateBirthDateUseCase: ValidateBirthDateUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val recaptchaManager: RecaptchaManager,
) : UseCase<SignUpUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName.split(' ').firstOrNull()?.trim().orEmpty()
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val password = params.password
        val receiveEmails = params.receiveEmails
        val receiveSms = params.receiveSms
        Timber.v(
            "Sign up. First name: $firstName, birth date: $birthDate, email: $email, phone: $phone, " +
                    "password: $password, receive emails: $receiveEmails, receive SMS: $receiveSms"
        )

        val firstNameValidationException =
            validateFirstNameUseCase(ValidateFirstNameUseCase.Params(firstName)).exceptionOrNull()
        val birthDateValidationException = if (birthDate == null) {
            EmptyDateException()
        } else {
            validateBirthDateUseCase(ValidateBirthDateUseCase.Params(birthDate)).exceptionOrNull()
        }
        val emailValidationException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()
        val phoneValidationException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()
        val passwordValidationException =
            validatePasswordUseCase(ValidatePasswordUseCase.Params(password)).exceptionOrNull()

        val validationException = ValidationException.from(
            firstNameValidationException,
            birthDateValidationException,
            emailValidationException,
            phoneValidationException,
            passwordValidationException,
        )
        if (validationException != null) throw validationException

        val recaptchaToken = recaptchaManager.execute(RecaptchaManager.ACTION_SIGN_UP)

        userRepository.signUp(
            firstName = firstName,
            birthDate = birthDate ?: USER_BIRTH_DATE_DEFAULT,
            email = email,
            phone = phone,
            password = password,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            recaptchaToken = recaptchaToken,
        )
    }

    data class Params(
        val firstName: String,
        val birthDate: LocalDate?,
        val email: Email,
        val phone: PhoneNumber,
        val password: String,
        val receiveEmails: Boolean,
        val receiveSms: Boolean,
    )
}
