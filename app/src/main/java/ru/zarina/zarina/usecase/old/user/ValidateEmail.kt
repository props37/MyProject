package ru.zarina.zarina.usecase.old.user

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.exception.validation.EmptyException
import ru.zarina.zarina.domain.old.exception.validation.FormatException
import ru.zarina.zarina.base.usecase.UseCase

@Factory
class ValidateEmailUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
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


