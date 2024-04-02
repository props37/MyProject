package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Email
import ru.zarina.zarina.domain.exception.EmptyEmailException
import ru.zarina.zarina.domain.exception.InvalidEmailException
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
) : UseCase<ValidateEmailUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val email = params.email.value.trim()
        when {
            email.isBlank() -> throw EmptyEmailException()
            !email.matches(EMAIL_REGEX_PATTERN.toRegex()) -> throw InvalidEmailException()
        }
    }
    
    data class Params(val email: Email)

    companion object {
        private const val EMAIL_REGEX_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    }
}
