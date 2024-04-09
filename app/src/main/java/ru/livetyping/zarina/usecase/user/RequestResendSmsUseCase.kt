package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class RequestResendSmsUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<RequestResendSmsUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Request SMS OTP resend for phone $phone")
        userRepository.requestResendSmsOtp(phone)
    }

    data class Params(val phone: PhoneNumber)
}
