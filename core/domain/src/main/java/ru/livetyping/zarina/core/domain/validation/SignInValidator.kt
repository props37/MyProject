package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.SignInByEmailParams
import ru.livetyping.zarina.core.domain.model.user.SignInParams
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException

/**
 * @throws EmailException
 * @throws PasswordException
 * @throws CombinedValidationException
 */
public class SignInValidator : Validator<SignInParams> {
    override fun validate(input: SignInParams) {
        when (input) {
            is SignInByEmailParams -> validateSignInByEmail(input)
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
}
