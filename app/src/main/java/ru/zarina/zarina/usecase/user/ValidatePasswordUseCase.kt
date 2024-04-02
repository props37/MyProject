package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.user.exception.EmptyPasswordException
import ru.zarina.zarina.domain.user.exception.PasswordTooShortException
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidatePasswordUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val password = params.password.trim()
        when {
            password.isBlank() -> throw EmptyPasswordException()
            password.length < PASSWORD_MIN_LENGTH -> throw PasswordTooShortException()
        }
    }

    data class Params(val password: String)

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}
