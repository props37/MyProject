package ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.user.ConfirmPhoneNumberUseCase
import ru.livetyping.zarina.usecase.user.RequestResendPhoneNumberChangeSmsOtpUseCase
import javax.inject.Inject

class PhoneNumberConfirmationInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmPhoneNumber: ConfirmPhoneNumberUseCase,
    val requestNewOtp: RequestResendPhoneNumberChangeSmsOtpUseCase,
)
