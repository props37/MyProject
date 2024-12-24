package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.model.user.exception.BirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.OldPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneException
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserInfoUseCase.Params
import ru.livetyping.zarina.core.domain.validation.BirthDateValidator
import ru.livetyping.zarina.core.domain.validation.EmailValidator
import ru.livetyping.zarina.core.domain.validation.FirstNameValidator
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
        val firstName = params.firstName?.trim()?.split(' ')?.first()
        val lastName = params.lastName?.trim()?.split(' ')?.first()
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val gender = params.gender
        val oldPassword = params.oldPassword
        val newPassword = params.newPassword

        // TODO: [Medium] Validate last name?
        validate(firstName, birthDate, email, phone, oldPassword, newPassword)

        val currentUser = userRepository.getUserFlow(CachePolicy.Remote()).firstOrNull()
        checkNotNull(currentUser) { "Failed to get current user" }

        userRepository.updateUserInfo(
            firstName = firstName ?: currentUser.firstName ?: "",
            lastName = lastName ?: currentUser.lastName ?: "",
            birthDate = birthDate ?: currentUser.birthDate ?: User.BIRTH_DATE_MIN_VALUE,
            email = email ?: currentUser.email,
            phone = phone ?: currentUser.phone ?: PhoneNumber.create(""),
            gender = gender ?: currentUser.gender,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }

    private fun validate(
        firstName: String?,
        birthDate: LocalDate?,
        email: Email?,
        phone: PhoneNumber?,
        oldPassword: String?,
        newPassword: String?,
    ) {
        val firstNameException = firstName?.let {
            try {
                FirstNameValidator().validate(it)
                null
            } catch (e: FirstNameException) {
                e
            }
        }
        val birthDateException = birthDate?.let {
            try {
                BirthDateValidator().validate(it)
                null
            } catch (e: BirthDateException) {
                e
            }
        }
        val emailException = email?.let {
            try {
                EmailValidator().validate(it)
                null
            } catch (e: EmailException) {
                e
            }
        }
        val phoneException = phone?.let {
            try {
                PhoneValidator().validate(it)
                null
            } catch (e: PhoneException) {
                e
            }
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
            firstNameException,
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
