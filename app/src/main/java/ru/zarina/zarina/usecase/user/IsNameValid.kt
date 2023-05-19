package ru.zarina.zarina.usecase.user

import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.IllegalContentsException
import ru.zarina.zarina.domain.exception.validation.TooLongException
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {

    private val allowedCharactersRegex = ALLOWED_CHARACTERS.toRegex()

    operator fun invoke(name: String) = runCatching {
        when {
            name.isEmpty() -> throw EmptyException("Name can't be empty")
            name.matches(allowedCharactersRegex) -> throw IllegalContentsException("Name contains illegal characters")
            name.length > 50 ->
                throw TooLongException("Name can't be longer than 50 characters", 50)

            else -> Result.success(Unit)
        }
    }

    companion object {
        private const val ALLOWED_CHARACTERS = "^[А-Яа-яЁё-]*\$"
    }
}
