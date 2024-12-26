package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.ChangePhoneNumberUseCase.Params
import ru.livetyping.zarina.core.domain.validation.PhoneValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ChangePhoneNumberUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ChangePhoneNumberUseCase {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        PhoneValidator().validate(phone)
        userRepository.changePhoneNumber(phone, params.yandexCaptchaToken)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ChangePhoneNumberUseCaseImpl"
    }
}
