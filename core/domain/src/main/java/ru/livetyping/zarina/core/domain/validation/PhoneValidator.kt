package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidPhoneException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneException

/**
 * @throws PhoneException
 */
public class PhoneValidator : Validator<PhoneNumber> {
    override fun validate(input: PhoneNumber) {
        val phoneString = input.value
        when {
            phoneString.isBlank() -> throw EmptyPhoneException()
            phoneString.all { it.isDigit() || it == PLUS } -> throw InvalidPhoneException()
            phoneString.length < PHONE_MIN_LENGTH -> throw InvalidPhoneException()
        }
    }

    private companion object {
        private const val PLUS = '+'
        private const val PHONE_MIN_LENGTH = 11
    }
}
