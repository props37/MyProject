package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.FormatException
import javax.inject.Inject

class ValidatePhoneUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
) : UseCase<ValidatePhoneUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val (phone) = params

        when {
            phone.isEmpty() || phone == "+7" -> throw EmptyException("Phone can't be empty")
            !phone.matches(PHONE_NUMBER_PATTERN) -> throw FormatException("Phone format is invalid")
        }
    }

    data class Params(
        val phone: String,
    )

    companion object {
        private val PHONE_NUMBER_PATTERN = "\\+7\\d{10}".toRegex()
    }

}
