package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class PhoneConfirmationDependencies @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmSignIn: ConfirmSignInUseCase,
    val requestNewOtp: RequestNewAuthOtpUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
)
