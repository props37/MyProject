package ru.livetyping.zarina.presentation.screen.checkout.recipient

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.Customer

sealed class CheckoutCustomerScreenAction {
    data object CheckoutClosed : CheckoutCustomerScreenAction()

    data class CustomerValidated(
        val cartType: CartType,
        val step: Int,
        val customer: Customer,
    ) : CheckoutCustomerScreenAction()
}
