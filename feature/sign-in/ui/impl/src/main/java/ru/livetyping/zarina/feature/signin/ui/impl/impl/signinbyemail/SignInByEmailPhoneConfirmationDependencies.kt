package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignInByEmailPhoneConfirmationDependencies @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
)
