package ru.livetyping.zarina.usecase.user

import android.telephony.PhoneNumberUtils
import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.InvalidPhoneNumberException
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
