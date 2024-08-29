package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.validation.LastNameValidator
import javax.inject.Inject

class ValidateLastNameUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateLastNameUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val validator = LastNameValidator()
        validator.validate(params.lastName)
    }

    data class Params(val lastName: String)
}
