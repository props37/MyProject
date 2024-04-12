package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.usecase.authorization.SetAuthorizationTokensUseCase
import timber.log.Timber
import javax.inject.Inject

class ConfirmSignInByPhoneUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val setAuthorizationTokensUseCase: SetAuthorizationTokensUseCase,
    private val setUserUseCase: SetUserUseCase,
) : UseCase<ConfirmSignInByPhoneUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        val otp = params.otp
        Timber.v("Confirm sign in by phone. Phone: $phone, otp: $otp")

        val authorizationResult = userRepository.confirmSignInByPhone(phone, otp)
        val authTokens = authorizationResult.tokens
        val user = authorizationResult.user

        val setAuthorizationTokensParams = SetAuthorizationTokensUseCase.Params(authTokens)
        setAuthorizationTokensUseCase(setAuthorizationTokensParams).getOrThrow()

        val setUserParams = SetUserUseCase.Params(user)
        setUserUseCase(setUserParams).getOrThrow()
    }

    data class Params(
        val phone: PhoneNumber,
        val otp: String,
    )
}
