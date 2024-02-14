package ru.zarina.zarina.usecase.rework.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.user.EmptyFirstNameException
import ru.zarina.zarina.domain.rework.user.InvalidFirstNameException
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class ValidateFirstNameUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateFirstNameUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val name = params.name
        when {
            name.isEmpty() -> throw EmptyFirstNameException()
            !name.matches(FIRST_NAME_REGEX_PATTERN.toRegex()) -> throw InvalidFirstNameException()
        }
    }

    data class Params(val name: String)

    companion object {
        private const val FIRST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
