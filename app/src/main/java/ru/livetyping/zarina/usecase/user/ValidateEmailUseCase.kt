package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.validation.EmailValidator
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateEmailUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val validator = EmailValidator()
        validator.validate(params.email)
    }
    
    data class Params(val email: Email)
}
