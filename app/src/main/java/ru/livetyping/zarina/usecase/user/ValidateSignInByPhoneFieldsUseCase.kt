package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import timber.log.Timber
import javax.inject.Inject

class ValidateSignInByPhoneFieldsUseCase @Inject constructor(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
) : UseCase<ValidateSignInByPhoneFieldsUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Validate sign in by phone fields. Phone: $phone")

        val phoneValidationException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()

        val validationException = ValidationException.from(phoneValidationException)
        if (validationException != null) throw validationException
    }

    data class Params(val phone: PhoneNumber)
}
