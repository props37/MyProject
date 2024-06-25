package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.user.ConfirmPhoneNumberChangeUseCase
import javax.inject.Inject

class ChangePhoneNumberOtpInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val confirmPhoneNumberChange: ConfirmPhoneNumberChangeUseCase,
)
