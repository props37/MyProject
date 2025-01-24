package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.PhoneNumber
import javax.inject.Inject

class RequestPhoneConfirmationUseCase @Inject constructor(
    private val userRepository: UserRepository,
) : UseCase<RequestPhoneConfirmationUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        userRepository.requestPhoneNumberConfirmation(params.phone, params.yandexCaptchaToken)
    }

    data class Params(
        val phone: PhoneNumber,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
