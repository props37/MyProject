package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaSignInMethod
import ru.livetyping.zarina.data.mindbox.MindboxApi
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class ConfirmSignInByPhoneUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val setUserWithAuthorizationTokensUseCase: SetUserWithAuthorizationTokensUseCase,
    private val mindboxApi: MindboxApi,
) : UseCase<ConfirmSignInByPhoneUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        val otp = params.otp
        Timber.v("Confirm sign in by phone. Phone: $phone, otp: $otp")

        val authorizationResult = userRepository.confirmSignInByPhone(phone, otp)
        val tokens = authorizationResult.tokens
        val user = authorizationResult.user

        mindboxApi.userSignedIn(user)

        val setUserWithTokensParams = SetUserWithAuthorizationTokensUseCase.Params(user, tokens)
        setUserWithAuthorizationTokensUseCase(setUserWithTokensParams).getOrThrow()
        AppMetricaHelper.reportUserSignedIn(AppMetricaSignInMethod.PHONE)
    }

    data class Params(
        val phone: PhoneNumber,
        val otp: String,
    )
}
