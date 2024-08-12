package ru.livetyping.zarina.presentation.screen.checkout.recipient

import ru.livetyping.zarina.domain.cart.CartType

sealed class CheckoutRecipientScreenAction {
    data object CheckoutClosed : CheckoutRecipientScreenAction()

    data class RecipientValidated(
        val cartType: CartType,
        val step: Int,
    ) : CheckoutRecipientScreenAction()
}
