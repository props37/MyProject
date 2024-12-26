package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException

/**
 * @throws PhoneNumberException
 */
public class PhoneValidator : Validator<PhoneNumber> {
    override fun validate(input: PhoneNumber) {
        val phoneString = input.value
        when {
            phoneString.isBlank() -> throw EmptyPhoneNumberException()
            !phoneString.matches(PHONE_REGEX_PATTERN.toRegex()) -> throw InvalidPhoneNumberException()
            phoneString.length !in PHONE_MIN_LENGTH..PHONE_MAX_LENGTH -> {
                throw InvalidPhoneNumberException()
            }
        }
    }

    private companion object {
        // Source: android.telephony.PhoneNumberUtils.GLOBAL_PHONE_NUMBER_PATTERN
        private const val PHONE_REGEX_PATTERN = "[\\+]?[0-9.-]+"
        private const val PHONE_MIN_LENGTH = 11
        private const val PHONE_MAX_LENGTH = 16
    }
}
