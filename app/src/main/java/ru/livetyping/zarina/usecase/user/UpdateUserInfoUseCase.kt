package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.validation.OldPasswordValidator
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val userRepository: UserRepository,
) : UseCase<UpdateUserInfoUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName
        val middleName = params.middleName
        val lastName = params.lastName
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val gender = params.gender
        val oldPassword = params.oldPassword
        val newPassword = params.newPassword
        Timber.v(
            "Update user info. First name: $firstName, middle name: $middleName, " +
                    "last name: $lastName, birth date: $birthDate, email: $email, " +
                    "phone: $phone, gender: $gender, old password: $oldPassword, " +
                    "new password: $newPassword"
        )

        val emailValidationException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()
        val phoneValidationException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()
        val oldPasswordValidationException = oldPassword?.let {
            val validator = OldPasswordValidator()
            runCatching { validator.validate(it) }.exceptionOrNull()
        }
        val newPasswordValidationException = newPassword?.let {
            validatePasswordUseCase(ValidatePasswordUseCase.Params(it)).exceptionOrNull()
        }

        val validationException = ValidationException.from(
            emailValidationException,
            phoneValidationException,
            oldPasswordValidationException,
            newPasswordValidationException,
        )
        if (validationException != null) throw validationException

        userRepository.updateUserInfo(
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            gender = gender,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate,
        val email: Email,
        val phone: PhoneNumber,
        val gender: Gender,
        val middleName: String? = null,
        val oldPassword: String? = null,
        val newPassword: String? = null,
    )
}
