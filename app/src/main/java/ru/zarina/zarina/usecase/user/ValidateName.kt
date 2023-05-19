package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.IllegalContentsException
import ru.zarina.zarina.domain.exception.validation.TooLongException
import javax.inject.Inject


class ValidateNameUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
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
