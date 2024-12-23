package ru.livetyping.zarina.core.domain.validation

import ru.livetyping.zarina.core.domain.model.user.exception.EmptyOldPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.OldPasswordException

/**
 * @throws OldPasswordException
 */
public class OldPasswordValidator : Validator<String> {
    override fun validate(input: String) {
        if (input.isBlank()) throw EmptyOldPasswordException()
    }
}
