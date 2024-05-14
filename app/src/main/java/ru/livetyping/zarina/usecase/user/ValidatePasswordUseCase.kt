package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.validation.PasswordValidator
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidatePasswordUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val validator = PasswordValidator()
        validator.validate(params.password)
    }

    data class Params(val password: String)
}
