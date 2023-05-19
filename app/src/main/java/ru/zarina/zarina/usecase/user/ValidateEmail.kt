package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.FormatException
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
) : UseCase<ValidateEmailUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (email) = params

        when {
            email.isEmpty() -> throw EmptyException("Email can't be empty")
            !email.matches(EMAIL_ADDRESS_PATTERN) -> throw FormatException("Email format is invalid")
        }
    }

    data class Params(
        val email: String,
    )

    companion object {
        private val EMAIL_ADDRESS_PATTERN = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+").toRegex()
    }
}
