package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewSignInByEmailPhoneNumberConfirmationOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignInByEmailConfirmationDependencies @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val requestNewSignInByEmailPhoneNumberConfirmationOtp: RequestNewSignInByEmailPhoneNumberConfirmationOtpUseCase,
    val confirmSignInByEmail: ConfirmSignInByEmailUseCase,
)
