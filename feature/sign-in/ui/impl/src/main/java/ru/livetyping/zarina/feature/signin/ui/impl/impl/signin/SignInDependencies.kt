package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import ru.livetyping.zarina.core.credential.CredentialManager
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestSignInPhoneConfirmationUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Inject

internal class SignInDependencies @Inject constructor(
    val credentialManager: CredentialManager,
    val signInByEmail: SignInByEmailUseCase,
    val signInByPhone: SignInByPhoneUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
    val smsCodeRetriever: SmsCodeRetriever,
    val requestSignInPhoneConfirmation: RequestSignInPhoneConfirmationUseCase,
)
