package ru.livetyping.zarina.ui.screen.signup.otp

import ru.livetyping.zarina.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.usecase.user.RequestResendSmsOtpUseCase
import javax.inject.Inject

class SignUpOtpInteractor @Inject constructor(
    val confirmSignUp: ConfirmSignUpUseCase,
    val requestResendSmsOtp: RequestResendSmsOtpUseCase,
)
