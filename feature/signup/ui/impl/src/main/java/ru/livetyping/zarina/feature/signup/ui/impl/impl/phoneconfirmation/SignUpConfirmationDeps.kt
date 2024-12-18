package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignUpConfirmationDeps @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignUp: ConfirmSignUpUseCase,
    val requestNewOtp: RequestNewAuthOtpUseCase,
)
