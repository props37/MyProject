package ru.zarina.zarina.ui.screens.subscribe

import ru.zarina.zarina.usecase.user.ValidateEmailUseCase
import ru.zarina.zarina.usecase.user.ValidateNameUseCase
import javax.inject.Inject

class SubscribeInteractor @Inject constructor(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
) {

    suspend fun validateName(name: String) = validateNameUseCase(ValidateNameUseCase.Params(name))

    suspend fun validateEmail(email: String) =
        validateEmailUseCase(ValidateEmailUseCase.Params(email))

}
