package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.user.exception.EmptyPasswordException
import ru.livetyping.zarina.domain.user.exception.PasswordTooShortException

class PasswordValidator : Validator<String> {
    override fun validate(input: String) {
        val trimmed = input.trim()
        when {
            trimmed.isBlank() -> throw EmptyPasswordException()
            trimmed.length < PASSWORD_MIN_LENGTH -> throw PasswordTooShortException()
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}
