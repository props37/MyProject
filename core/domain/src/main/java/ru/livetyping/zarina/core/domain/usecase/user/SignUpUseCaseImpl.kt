package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SignUpUseCase.Params
import ru.livetyping.zarina.core.domain.validation.SignUpValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import java.time.LocalDate

internal class SignUpUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SignUpUseCase {

    override suspend fun execute(params: Params) {
        val firstName = params.name.trim().split(' ').firstOrNull().orEmpty()
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val password = params.password
        val receiveEmails = params.receiveEmails
        val receiveSms = params.receiveSms

        validateFields(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
        )

        userRepository.signUp(
            firstName = firstName,
            birthDate = birthDate ?: User.BIRTH_DATE_MIN_VALUE,
            email = email,
            phone = phone,
            password = password,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            yandexCaptchaToken = params.yandexCaptchaToken,
        )
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private fun validateFields(
        firstName: String,
        birthDate: LocalDate?,
        email: Email,
        phone: PhoneNumber,
        password: String,
    ) {
        val signUpParams = SignUpValidator.Params(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
        )
        SignUpValidator().validate(signUpParams)
    }

    private companion object {
        private const val TAG = "SignUpUseCaseImpl"
    }
}
