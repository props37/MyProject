package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class PhoneConfirmationDeps @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignUp: ConfirmSignUpUseCase,
    val requestNewOtp: RequestNewAuthOtpUseCase,
)
