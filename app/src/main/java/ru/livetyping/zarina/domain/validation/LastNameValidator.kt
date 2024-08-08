package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.user.exception.EmptyLastNameException
import ru.livetyping.zarina.domain.user.exception.InvalidLastNameException

class LastNameValidator : Validator<String> {
    override fun validate(input: String) {
        val trimmed = input.trim()
        when {
            trimmed.isBlank() -> throw EmptyLastNameException()
            !trimmed.matches(LAST_NAME_REGEX_PATTERN.toRegex()) -> {
                throw InvalidLastNameException()
            }
        }
    }

    companion object {
        private const val LAST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
