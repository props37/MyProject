package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateSignUpFieldsUseCase: ValidateSignUpFieldsUseCase,
) : UseCase<SignUpUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val firstName = params.firstName.split(' ').firstOrNull()?.trim().orEmpty()
        val birthDate = params.birthDate
        val email = params.email
        val phone = params.phone
        val password = params.password
        val receiveEmails = params.receiveEmails
        val receiveSms = params.receiveSms
        Timber.v(
            "Sign up. First name: $firstName, birth date: $birthDate, email: $email, phone: $phone, " +
                    "password: $password, receive emails: $receiveEmails, receive SMS: $receiveSms"
        )

        val validationParams = ValidateSignUpFieldsUseCase.Params(
            firstName = firstName,
            birthDate = birthDate,
            email = email,
            phone = phone,
            password = password,
        )
        validateSignUpFieldsUseCase(validationParams).getOrThrow()

        userRepository.signUp(
            firstName = firstName,
            birthDate = birthDate ?: User.BIRTH_DATE_DEFAULT,
            email = email,
            phone = phone,
            password = password,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            yandexCaptchaToken = params.yandexCaptchaToken,
        )
    }

    data class Params(
        val firstName: String,
        val birthDate: LocalDate?,
        val email: Email,
        val phone: PhoneNumber,
        val password: String,
        val receiveEmails: Boolean,
        val receiveSms: Boolean,
        val yandexCaptchaToken: YandexCaptchaToken,
    )
}
