package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.InvalidPhoneNumberException

// TODO: [Medium] Improve
class PhoneNumberValidator : Validator<PhoneNumber> {
    override fun validate(input: PhoneNumber) {
        val phoneString = input.value.trim()
        when {
            phoneString.isBlank() -> throw EmptyPhoneNumberException()
            !phoneString.matches(PHONE_REGEX_PATTERN.toRegex()) -> {
                throw InvalidPhoneNumberException()
            }
        }
    }

    companion object {
        // Source: android.telephony.PhoneNumberUtils.GLOBAL_PHONE_NUMBER_PATTERN
        private const val PHONE_REGEX_PATTERN = "[\\+]?[0-9.-]+"
    }
}
