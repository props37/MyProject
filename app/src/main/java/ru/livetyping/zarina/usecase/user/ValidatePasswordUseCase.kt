package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.user.exception.EmptyPasswordException
import ru.livetyping.zarina.domain.user.exception.PasswordTooShortException
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
