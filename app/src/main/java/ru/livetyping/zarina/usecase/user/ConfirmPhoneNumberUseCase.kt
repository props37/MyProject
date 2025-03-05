package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaSignInMethod
import ru.livetyping.zarina.data.mindbox.MindboxApi
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.common.PhoneNumber
import javax.inject.Inject

class ConfirmPhoneNumberUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val setUserWithAuthorizationTokensUseCase: SetUserWithAuthorizationTokensUseCase,
    private val mindboxApi: MindboxApi,
) : UseCase<ConfirmPhoneNumberUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val authResult = userRepository.confirmPhoneNumber(params.phone, params.code)
        val user = authResult.user
        val tokens = authResult.tokens

        mindboxApi.userSignedIn(user)

        val setUserWithTokensParams = SetUserWithAuthorizationTokensUseCase.Params(user, tokens)
        setUserWithAuthorizationTokensUseCase(setUserWithTokensParams).getOrThrow()
        AppMetricaHelper.reportUserSignedIn(AppMetricaSignInMethod.PASSWORD)
    }

    data class Params(val phone: PhoneNumber, val code: String)
}
