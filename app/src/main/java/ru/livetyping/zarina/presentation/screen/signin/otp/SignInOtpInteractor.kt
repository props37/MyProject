package ru.livetyping.zarina.presentation.screen.signin.otp

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.captcha.GetYandexCaptchaUseCase
import ru.livetyping.zarina.usecase.user.ConfirmSignInByPhoneUseCase
import ru.livetyping.zarina.usecase.user.RequestResendAuthorizationSmsOtpUseCase
import javax.inject.Inject

class SignInOtpInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignInByPhone: ConfirmSignInByPhoneUseCase,
    val requestResendSmsOtp: RequestResendAuthorizationSmsOtpUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
)
