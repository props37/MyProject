package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.validation.FirstNameValidator
import javax.inject.Inject

class ValidateFirstNameUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateFirstNameUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val validator = FirstNameValidator()
        validator.validate(params.firstName)
    }

    data class Params(val firstName: String)
}
