package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.BirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.OldPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneException
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserInfoUseCase.Params
import ru.livetyping.zarina.core.domain.validation.BirthDateValidator
import ru.livetyping.zarina.core.domain.validation.EmailValidator
import ru.livetyping.zarina.core.domain.validation.OldPasswordValidator
import ru.livetyping.zarina.core.domain.validation.PasswordValidator
import ru.livetyping.zarina.core.domain.validation.PhoneValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import java.time.LocalDate

internal class UpdateUserInfoUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), UpdateUserInfoUseCase {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName
        val lastName = params.lastName
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val gender = params.gender
        val oldPassword = params.oldPassword
        val newPassword = params.newPassword

        // TODO: [Medium] Validate first name and last name?
        validate(birthDate, email, phone, oldPassword, newPassword)

        userRepository.updateUserInfo(
            firstName = firstName,
            middleName = null,
            lastName = lastName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            gender = gender,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }

    private fun validate(
        birthDate: LocalDate,
        email: Email,
        phone: PhoneNumber,
        oldPassword: String?,
        newPassword: String?,
    ) {
        val birthDateException = try {
            BirthDateValidator().validate(birthDate)
            null
        } catch (e: BirthDateException) {
            e
        }
        val emailException = try {
            EmailValidator().validate(email)
            null
        } catch (e: EmailException) {
            e
        }
        val phoneException = try {
            PhoneValidator().validate(phone)
            null
        } catch (e: PhoneException) {
            e
        }
        val oldPasswordException = oldPassword?.let { password ->
            try {
                OldPasswordValidator().validate(password)
                null
            } catch (e: OldPasswordException) {
                e
            }
        }
        val newPasswordException = newPassword?.let { password ->
            try {
                PasswordValidator().validate(password)
                null
            } catch (e: PasswordException) {
                e
            }
        }

        val exceptions = listOfNotNull(
            birthDateException,
            emailException,
            phoneException,
            oldPasswordException,
            newPasswordException,
        )
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "UpdateUserInfoUseCaseImpl"
    }
}
