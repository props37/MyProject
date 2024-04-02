package ru.zarina.zarina.usecase.user

import android.telephony.PhoneNumberUtils
import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.PhoneNumber
import ru.zarina.zarina.domain.exception.EmptyPhoneNumberException
import ru.zarina.zarina.domain.exception.InvalidPhoneNumberException
import javax.inject.Inject

class ValidatePhoneNumberUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidatePhoneNumberUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phoneNumber.value.trim()
        when {
            phone.isBlank() -> throw EmptyPhoneNumberException()
            !PhoneNumberUtils.isGlobalPhoneNumber(phone) -> throw InvalidPhoneNumberException()
        }
    }

    data class Params(val phoneNumber: PhoneNumber)
}
