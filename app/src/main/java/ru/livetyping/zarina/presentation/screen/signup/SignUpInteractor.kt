package ru.livetyping.zarina.presentation.screen.signup

import ru.livetyping.zarina.presentation.common.credentialmanager.CredentialManager
import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.user.SignUpUseCase
import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val credentialManager: CredentialManager,
    val signUp: SignUpUseCase,
)
