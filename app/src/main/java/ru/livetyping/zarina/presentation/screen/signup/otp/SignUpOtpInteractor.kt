package ru.livetyping.zarina.presentation.screen.signup.otp

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.usecase.user.RequestResendSmsOtpUseCase
import javax.inject.Inject

class SignUpOtpInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignUp: ConfirmSignUpUseCase,
    val requestResendSmsOtp: RequestResendSmsOtpUseCase,
)
