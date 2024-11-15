package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class ChangePhoneNumberUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val validatePhoneChangeFieldsUseCase: ValidatePhoneChangeFieldsUseCase,
    private val userRepository: UserRepository,
) : UseCase<ChangePhoneNumberUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Change phone number to $phone")

        val validationParams = ValidatePhoneChangeFieldsUseCase.Params(phone)
        validatePhoneChangeFieldsUseCase(validationParams).getOrThrow()

        userRepository.changePhoneNumber(phone, params.yandexCaptchaToken)
    }

    data class Params(
        val phone: PhoneNumber,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
