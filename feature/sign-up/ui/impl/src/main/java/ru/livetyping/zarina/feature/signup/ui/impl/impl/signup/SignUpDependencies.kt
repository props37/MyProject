package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.credential.CredentialManager
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignUpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignUpDependencies @Inject constructor(
    val signUp: SignUpUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
    val credentialManager: CredentialManager,
    val smsCodeRetriever: SmsCodeRetriever,
)
