package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.usecase.user.ValidateEmailUseCase
import ru.livetyping.zarina.usecase.user.ValidateFirstNameUseCase
import ru.livetyping.zarina.usecase.user.ValidateLastNameUseCase
import ru.livetyping.zarina.usecase.user.ValidatePhoneNumberUseCase
import timber.log.Timber
import javax.inject.Inject

class ValidateRecipientUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateLastNameUseCase: ValidateLastNameUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : UseCase<ValidateRecipientUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val customer = params.customer
        Timber.v("Validate recipient: $customer")

        val firstNameException =
            validateFirstNameUseCase(ValidateFirstNameUseCase.Params(customer.firstName)).exceptionOrNull()
        val lastNameException =
            validateLastNameUseCase(ValidateLastNameUseCase.Params(customer.lastName)).exceptionOrNull()
        val phoneException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(customer.phone)).exceptionOrNull()
        val emailException =
            validateEmailUseCase(ValidateEmailUseCase.Params(customer.email)).exceptionOrNull()

        val validationException = ValidationException.from(
            firstNameException,
            lastNameException,
            phoneException,
            emailException,
        )
        if (validationException != null) throw validationException
    }

    data class Params(
        val customer: Customer,
    )
}
