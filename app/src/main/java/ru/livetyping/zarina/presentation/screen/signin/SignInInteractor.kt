package ru.livetyping.zarina.presentation.screen.signin

import ru.livetyping.zarina.presentation.common.credentialmanager.CredentialManager
import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.captcha.FetchYandexCaptchaUseCase
import ru.livetyping.zarina.usecase.captcha.GetYandexCaptchaFlowUseCase
import ru.livetyping.zarina.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.usecase.user.ValidateSignInByEmailFieldsUseCase
import ru.livetyping.zarina.usecase.user.ValidateSignInByPhoneFieldsUseCase
import javax.inject.Inject

class SignInInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val credentialManager: CredentialManager,
    val validateSignInByEmailFields: ValidateSignInByEmailFieldsUseCase,
    val signInByEmail: SignInByEmailUseCase,
    val validateSignInByPhoneFields: ValidateSignInByPhoneFieldsUseCase,
    val signInByPhone: SignInByPhoneUseCase,
    val fetchYandexCaptcha: FetchYandexCaptchaUseCase,
    val getYandexCaptchaFlow: GetYandexCaptchaFlowUseCase,
)
