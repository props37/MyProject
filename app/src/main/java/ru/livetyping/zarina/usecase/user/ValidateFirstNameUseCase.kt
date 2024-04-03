package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.InvalidFirstNameException
import javax.inject.Inject

class ValidateFirstNameUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateFirstNameUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName.trim()
        when {
            firstName.isBlank() -> throw EmptyFirstNameException()
            !firstName.matches(FIRST_NAME_REGEX_PATTERN.toRegex()) -> {
                throw InvalidFirstNameException()
            }
        }
    }

    data class Params(val firstName: String)

    companion object {
        private const val FIRST_NAME_REGEX_PATTERN = "^[А-Яа-яЁё-]*\$"
    }
}
