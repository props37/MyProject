package ru.livetyping.zarina.usecase.orderplacement

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
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
        val firstName = params.firstName
        val lastName = params.lastName
        val phone = params.phone
        val email = params.email
        Timber.v("Validate recipient. First name: $firstName, last name: $lastName, phone: $phone, email: $email")

        val firstNameException =
            validateFirstNameUseCase(ValidateFirstNameUseCase.Params(firstName)).exceptionOrNull()
        val lastNameException =
            validateLastNameUseCase(ValidateLastNameUseCase.Params(lastName)).exceptionOrNull()
        val phoneException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()
        val emailException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()

        val validationException = ValidationException.from(
            firstNameException,
            lastNameException,
            phoneException,
            emailException,
        )
        if (validationException != null) throw validationException
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val phone: PhoneNumber,
        val email: Email,
    )
}
