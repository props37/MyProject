package ru.livetyping.zarina.domain.validation

import ru.livetyping.zarina.domain.user.exception.EmptyOldPasswordException

class OldPasswordValidator : Validator<String> {
    override fun validate(input: String) {
        val trimmed = input.trim()
        when {
            trimmed.isBlank() -> throw EmptyOldPasswordException()
        }
    }
}
