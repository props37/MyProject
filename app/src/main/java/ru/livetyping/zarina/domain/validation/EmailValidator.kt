package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.InvalidEmailException

class EmailValidator : Validator<Email> {
    override fun validate(input: Email) {
        val emailString = input.value.trim()
        when {
            emailString.isBlank() -> throw EmptyEmailException()
            !emailString.matches(EMAIL_REGEX_PATTERN.toRegex()) -> throw InvalidEmailException()
        }
    }

    companion object {
        // Source: android.util.Patterns.EMAIL_ADDRESS
        private const val EMAIL_REGEX_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    }
}
