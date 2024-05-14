package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.exception.InvalidDateException
import java.time.LocalDate
import javax.inject.Inject

class ValidateBirthDateUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateBirthDateUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val currentDate = LocalDate.now()
        if (params.birthDate > currentDate) {
            throw InvalidDateException("Birth date can not be greater than the current date")
        }
    }

    data class Params(val birthDate: LocalDate)
}
