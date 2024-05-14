package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.validation.PhoneNumberValidator
import javax.inject.Inject

class ValidatePhoneNumberUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidatePhoneNumberUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val validator = PhoneNumberValidator()
        validator.validate(params.phoneNumber)
    }

    data class Params(val phoneNumber: PhoneNumber)
}
