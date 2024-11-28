package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.BirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyBirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneException
import java.time.LocalDate

/**
 * @throws FirstNameException
 * @throws BirthDateException
 * @throws EmailException
 * @throws PhoneException
 * @throws PasswordException
 * @throws CombinedValidationException
 */
public class SignUpValidator : Validator<SignUpValidator.Params> {
    override fun validate(input: Params) {
        val firstNameException = try {
            FirstNameValidator().validate(input.firstName)
            null
        } catch (e: FirstNameException) {
            e
        }
        val birthDateException = try {
            if (input.birthDate == null) throw EmptyBirthDateException()
            BirthDateValidator().validate(input.birthDate)
            null
        } catch (e: BirthDateException) {
            e
        }
        val emailException = try {
            EmailValidator().validate(input.email)
            null
        } catch (e: EmailException) {
            e
        }
        val phoneException = try {
            PhoneValidator().validate(input.phone)
            null
        } catch (e: PhoneException) {
            e
        }
        val passwordException = try {
            PasswordValidator().validate(input.password)
            null
        } catch (e: PasswordException) {
            e
        }

        val exceptions = listOfNotNull(
            firstNameException,
            birthDateException,
            emailException,
            phoneException,
            passwordException,
        )
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }

    public data class Params(
        val firstName: String,
        val birthDate: LocalDate?,
        val email: Email,
        val phone: PhoneNumber,
        val password: String,
    )
}
