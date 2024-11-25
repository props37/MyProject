package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordTooShortException

public class PasswordValidator : Validator<String> {
    override fun validate(input: String) {
        when {
            input.isBlank() -> throw EmptyPasswordException()
            input.contains(SPACE, ignoreCase = true) -> throw InvalidPasswordException()
            input.length < PASSWORD_MIN_LENGTH -> throw PasswordTooShortException()
        }
    }

    private companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val SPACE = ' '
    }
}