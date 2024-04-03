package ru.livetyping.zarina.ui.screens.subscribe

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.Barcode
import ru.livetyping.zarina.usecase.old.subscription.SubscribeToOfferUseCase
import ru.livetyping.zarina.usecase.old.user.ValidateEmailUseCase
import ru.livetyping.zarina.usecase.old.user.ValidateNameUseCase

@Factory
class SubscribeInteractor(
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
