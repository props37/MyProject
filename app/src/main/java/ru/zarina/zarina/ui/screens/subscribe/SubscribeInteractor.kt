package ru.zarina.zarina.ui.screens.subscribe

import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.usecase.subscription.SubscribeToOfferUseCase
import ru.zarina.zarina.usecase.user.ValidateEmailUseCase
import ru.zarina.zarina.usecase.user.ValidateNameUseCase
import javax.inject.Inject

class SubscribeInteractor @Inject constructor(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val subscribeToOfferUseCase: SubscribeToOfferUseCase,
) {

    suspend fun validateName(name: String) = validateNameUseCase(ValidateNameUseCase.Params(name))

    suspend fun validateEmail(email: String) =
        validateEmailUseCase(ValidateEmailUseCase.Params(email))

    suspend fun subscribeToOffer(offerBarcode: Barcode, name: String, email: String) =
        subscribeToOfferUseCase(
            SubscribeToOfferUseCase.Params(
                offerBarcode = offerBarcode,
                name = name,
                email = email
            )
        )

}
