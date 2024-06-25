package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class ConfirmPhoneNumberChangeUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<ConfirmPhoneNumberChangeUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        val code = params.code
        Timber.v("Confirm phone number change. Phone: $phone, code: $code")

        userRepository.confirmPhoneNumberChange(phone, code)
    }

    data class Params(val phone: PhoneNumber, val code: String)
}
