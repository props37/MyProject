package ru.livetyping.zarina.presentation.screen.signin

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.usecase.user.SignInByPhoneUseCase
import javax.inject.Inject

class SignInInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val signInByEmail: SignInByEmailUseCase,
    val signInByPhone: SignInByPhoneUseCase,
)
