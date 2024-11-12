package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.EmptyDateException
import ru.livetyping.zarina.domain.common.exception.ValidationException
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class ValidateSignUpFieldsUseCase @Inject constructor(
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateBirthDateUseCase: ValidateBirthDateUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : UseCase<ValidateSignUpFieldsUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName.split(' ').firstOrNull()?.trim().orEmpty()
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val password = params.password
        Timber.v(
            "Validate sign up fields. First name: $firstName, birth date: $birthDate, email: $email, phone: $phone, " +
                    "password: $password"
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
    }

    data class Params(
        val firstName: String,
        val birthDate: LocalDate?,
        val email: Email,
        val phone: PhoneNumber,
        val password: String,
    )
}
