package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.DeliveryMethod

sealed class CheckoutDeliveryMethodScreenAction {
    data object ScreenClosed : CheckoutDeliveryMethodScreenAction()

    data object CheckoutClosed : CheckoutDeliveryMethodScreenAction()

    data class DeliveryMethodSelected(
        val cartType: CartType,
        val step: Int,
        val method: DeliveryMethod,
        val customer: Customer,
    ) : CheckoutDeliveryMethodScreenAction()
}
