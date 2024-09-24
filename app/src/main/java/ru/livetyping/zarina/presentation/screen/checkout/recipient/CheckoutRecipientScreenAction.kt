package ru.livetyping.zarina.presentation.screen.checkout.recipient

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.Customer

sealed class CheckoutRecipientScreenAction {
    data object CheckoutClosed : CheckoutRecipientScreenAction()

    data class RecipientValidated(
        val cartType: CartType,
        val step: Int,
        val customer: Customer,
    ) : CheckoutRecipientScreenAction()
}
