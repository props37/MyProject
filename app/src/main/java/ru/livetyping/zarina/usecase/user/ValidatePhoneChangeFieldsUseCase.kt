package ru.livetyping.zarina.usecase.user

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.domain.common.PhoneNumber
import timber.log.Timber
import javax.inject.Inject

class ValidatePhoneChangeFieldsUseCase @Inject constructor(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
) : UseCase<ValidatePhoneChangeFieldsUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val phone = params.phone
        Timber.v("Validate phone change fields. Phone: $phone")

        val validationException =
            validatePhoneNumberUseCase(ValidatePhoneNumberUseCase.Params(phone)).exceptionOrNull()
        if (validationException != null) throw validationException
    }

    data class Params(val phone: PhoneNumber)
}
