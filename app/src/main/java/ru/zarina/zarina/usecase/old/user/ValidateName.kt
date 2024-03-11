package ru.zarina.zarina.usecase.old.user

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.exception.validation.EmptyException
import ru.zarina.zarina.domain.old.exception.validation.IllegalContentsException
import ru.zarina.zarina.domain.old.exception.validation.TooLongException
import ru.zarina.zarina.base.usecase.UseCase


@Factory
class ValidateNameUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
) : UseCase<ValidateNameUseCase.Params, Unit>(dispatcher) {

    private val allowedCharactersRegex = ALLOWED_CHARACTERS.toRegex()

    override suspend fun execute(params: Params) {
        val (name) = params

        when {
            name.isEmpty() -> throw EmptyException("Name can't be empty")
            !name.matches(allowedCharactersRegex) -> throw IllegalContentsException("Name contains illegal characters")
            name.length > 50 ->
                throw TooLongException("Name can't be longer than 50 characters", 50)

            else -> Result.success(Unit)
        }
    }

    data class Params(
        val name: String,
    )

    companion object {
        private const val ALLOWED_CHARACTERS = "^[А-Яа-яЁё-]*\$"
    }
}
