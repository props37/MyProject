package ru.livetyping.zarina.presentation.screen.signin.otp

import ru.livetyping.zarina.usecase.user.ConfirmSignInByPhoneUseCase
import ru.livetyping.zarina.usecase.user.RequestResendSmsOtpUseCase
import javax.inject.Inject

class SignInOtpInteractor @Inject constructor(
    val confirmSignInByPhone: ConfirmSignInByPhoneUseCase,
    val requestResendSmsOtp: RequestResendSmsOtpUseCase,
)
