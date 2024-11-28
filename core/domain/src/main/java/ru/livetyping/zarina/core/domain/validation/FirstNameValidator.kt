package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidFirstNameException

/**
 * @throws FirstNameException
 */
public class FirstNameValidator : Validator<String> {
    override fun validate(input: String) {
        when {
            input.isBlank() -> throw EmptyFirstNameException()
            !input.matches(FIRST_NAME_REGEX_PATTERN.toRegex()) -> {
                throw InvalidFirstNameException()
            }
        }
    }

    private companion object {
        private const val FIRST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
