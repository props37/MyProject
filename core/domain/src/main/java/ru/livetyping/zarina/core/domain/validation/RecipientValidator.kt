package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.LastNameException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException

/**
 * @throws FirstNameException
 * @throws LastNameException
 * @throws PhoneNumberException
 * @throws EmailException
 * @throws CombinedValidationException
 */
public class RecipientValidator : Validator<Recipient> {
    override fun validate(input: Recipient) {
        val firstNameException = try {
            FirstNameValidator().validate(input.firstName)
            null
        } catch (e: FirstNameException) {
            e
        }
        val lastNameValidator = try {
            LastNameValidator().validate(input.lastName)
            null
        } catch (e: LastNameException) {
            e
        }
        val phoneException = try {
            PhoneValidator().validate(input.phone)
            null
        } catch (e: PhoneNumberException) {
            e
        }
        val emailException = try {
            EmailValidator().validate(input.email)
            null
        } catch (e: EmailException) {
            e
        }

        val exceptions = listOfNotNull(firstNameException, lastNameValidator, phoneException, emailException)
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }
}
