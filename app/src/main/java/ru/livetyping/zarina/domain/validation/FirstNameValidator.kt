package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.InvalidFirstNameException

class FirstNameValidator : Validator<String> {
    override fun validate(input: String) {
        val trimmed = input.trim()
        when {
            trimmed.isBlank() -> throw EmptyFirstNameException()
            !trimmed.matches(FIRST_NAME_REGEX_PATTERN.toRegex()) -> {
                throw InvalidFirstNameException()
            }
        }
    }

    companion object {
        private const val FIRST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
