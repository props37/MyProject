package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class SignInByPhoneUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateSignInByPhoneFieldsUseCase: ValidateSignInByPhoneFieldsUseCase,
) : UseCase<SignInByPhoneUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Sign in by phone. Phone: $phone")

        val validationParams = ValidateSignInByPhoneFieldsUseCase.Params(phone)
        validateSignInByPhoneFieldsUseCase(validationParams).getOrThrow()

        userRepository.signIn(phone, params.yandexCaptchaToken)
    }

    data class Params(
        val phone: PhoneNumber,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
