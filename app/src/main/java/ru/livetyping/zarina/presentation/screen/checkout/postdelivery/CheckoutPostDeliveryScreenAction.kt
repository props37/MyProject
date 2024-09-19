package ru.livetyping.zarina.presentation.screen.checkout.postdelivery

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams

sealed class CheckoutPostDeliveryScreenAction {
    data object ScreenClosed : CheckoutPostDeliveryScreenAction()

    data object CheckoutClosed : CheckoutPostDeliveryScreenAction()

    data class ContinueClicked(
        val cartType: CartType,
        val step: Int,
        val checkoutParams: CourierDeliveryCheckoutParams,
    ) : CheckoutPostDeliveryScreenAction()
}
