package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class OtpDeps @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
)
