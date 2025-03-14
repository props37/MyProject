package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInByPhoneUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignInByPhonePhoneConfirmationDependencies @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignInByPhone: ConfirmSignInByPhoneUseCase,
    val requestNewOtp: RequestNewAuthOtpUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
)
