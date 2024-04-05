package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class ConfirmSignUpUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<ConfirmSignUpUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        val otp = params.otp
        Timber.v("Confirm sign up. Phone: $phone, otp: $otp")

        userRepository.confirmSignUp(phone, otp)
        // TODO: [High] Implement
    }

    data class Params(val phone: PhoneNumber, val otp: String)
}
