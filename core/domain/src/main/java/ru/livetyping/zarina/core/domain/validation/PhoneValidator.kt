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
            !phoneString.matches(PHONE_REGEX_PATTERN.toRegex()) -> throw InvalidPhoneException()
            phoneString.length < PHONE_MIN_LENGTH -> throw InvalidPhoneException()
        }
    }

    private companion object {
        // Source: android.telephony.PhoneNumberUtils.GLOBAL_PHONE_NUMBER_PATTERN
        private const val PHONE_REGEX_PATTERN = "[\\+]?[0-9.-]+"
        private const val PHONE_MIN_LENGTH = 11
    }
}
