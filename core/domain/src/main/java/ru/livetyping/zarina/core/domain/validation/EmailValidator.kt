package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidEmailException

public class EmailValidator : Validator<Email> {
    override fun validate(input: Email) {
        val emailString = input.value
        when {
            emailString.isBlank() -> throw EmptyEmailException()
            !emailString.matches(EMAIL_REGEX_PATTERN.toRegex()) -> throw InvalidEmailException()
        }
    }

    private companion object {
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
