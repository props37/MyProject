package ru.livetyping.zarina.presentation.screen.signup

import ru.livetyping.zarina.presentation.common.credentialmanager.CredentialManager
import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.captcha.FetchYandexCaptchaUseCase
import ru.livetyping.zarina.usecase.captcha.GetYandexCaptchaFlowUseCase
import ru.livetyping.zarina.usecase.user.SignUpUseCase
import ru.livetyping.zarina.usecase.user.ValidateSignUpFieldsUseCase
import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val credentialManager: CredentialManager,
    val validateSignUpFields: ValidateSignUpFieldsUseCase,
    val signUp: SignUpUseCase,
    val fetchYandexCaptcha: FetchYandexCaptchaUseCase,
    val getYandexCaptchaFlow: GetYandexCaptchaFlowUseCase,
)
