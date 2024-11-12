package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

import ru.livetyping.zarina.presentation.common.sms.SmsCodeRetriever
import ru.livetyping.zarina.usecase.captcha.GetYandexCaptchaUseCase
import ru.livetyping.zarina.usecase.user.ChangePhoneNumberUseCase
import ru.livetyping.zarina.usecase.user.ValidatePhoneChangeFieldsUseCase
import javax.inject.Inject

class ChangePhoneNumberInteractor @Inject constructor(
    val smsCodeRetriever: SmsCodeRetriever,
    val validatePhoneChangeFields: ValidatePhoneChangeFieldsUseCase,
    val changePhoneNumber: ChangePhoneNumberUseCase,
    val getYandexCaptcha: GetYandexCaptchaUseCase,
)
