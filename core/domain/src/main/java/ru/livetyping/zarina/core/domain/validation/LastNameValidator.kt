package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.user.exception.EmptyLastNameException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidLastNameException
import ru.livetyping.zarina.core.domain.model.user.exception.LastNameException

/**
 * @throws LastNameException
 */
public class LastNameValidator : Validator<String> {
    override fun validate(input: String) {
        when {
            input.isBlank() -> throw EmptyLastNameException()
            !input.matches(FIRST_NAME_REGEX_PATTERN.toRegex()) -> {
                throw InvalidLastNameException()
            }
        }
    }

    private companion object {
        private const val FIRST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
