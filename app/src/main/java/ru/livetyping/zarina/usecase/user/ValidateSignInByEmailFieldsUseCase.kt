package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.exception.ValidationException
import timber.log.Timber
import javax.inject.Inject

class ValidateSignInByEmailFieldsUseCase @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : UseCase<ValidateSignInByEmailFieldsUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val email = params.email
        val password = params.password
        Timber.v("Validate sign in by email fields. Email: $email, password: $password")

        val emailValidationException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()
        val passwordValidationException =
            validatePasswordUseCase(ValidatePasswordUseCase.Params(password)).exceptionOrNull()

        val validationException = ValidationException.from(
            emailValidationException,
            passwordValidationException,
        )
        if (validationException != null) throw validationException
    }

    data class Params(
        val email: Email,
        val password: String,
    )
}
