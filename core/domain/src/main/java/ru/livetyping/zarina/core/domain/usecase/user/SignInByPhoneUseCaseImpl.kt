package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.SignInByPhoneParams
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase.Params
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SignInByPhoneUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SignInByPhoneUseCase {

    override suspend fun execute(params: Params) {
        val phone = params.phone

        validateFields(phone)

        userRepository.signIn(phone)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private fun validateFields(phone: PhoneNumber) {
        val validator = SignInValidator()
        val signInParams = SignInByPhoneParams(phone)
        validator.validate(signInParams)
    }

    private companion object {
        private const val TAG = "SignInByPhoneUseCaseImpl"
    }
}
