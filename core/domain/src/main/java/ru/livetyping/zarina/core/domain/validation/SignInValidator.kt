package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException

/**
 * @throws EmailException
 * @throws PasswordException
 * @throws PhoneNumberException
 * @throws CombinedValidationException
 */
public class SignInValidator : Validator<SignInValidator.Params> {
    override fun validate(input: Params) {
        when (input) {
            is SignInByEmailParams -> validateSignInByEmail(input)
            is SignInByPhoneParams -> validateSignInByPhone(input)
        }
    }

    private fun validateSignInByEmail(params: SignInByEmailParams) {
        val emailException = try {
            val validator = EmailValidator()
            validator.validate(params.email)
            null
        } catch (e: EmailException) {
            e
        }
        val passwordException = try {
            val validator = PasswordValidator()
            validator.validate(params.password)
            null
        } catch (e: PasswordException) {
            e
        }

        val exceptions = listOfNotNull(emailException, passwordException)
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }

    private fun validateSignInByPhone(params: SignInByPhoneParams) {
        val phoneValidator = PhoneValidator()
        phoneValidator.validate(params.phone)
    }

    public sealed class Params

    public data class SignInByEmailParams(
        val email: Email,
        val password: String,
    ) : Params()

    public data class SignInByPhoneParams(val phone: PhoneNumber) : Params()
}
