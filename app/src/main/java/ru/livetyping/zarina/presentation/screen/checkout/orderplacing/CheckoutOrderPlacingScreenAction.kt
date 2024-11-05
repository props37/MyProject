package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.OrderDetails

sealed class CheckoutOrderPlacingScreenAction {
    data object ScreenClosed : CheckoutOrderPlacingScreenAction()

    data object CheckoutClosed : CheckoutOrderPlacingScreenAction()

    data object ChangeCustomerClicked : CheckoutOrderPlacingScreenAction()

    data object ChangeDeliveryClicked : CheckoutOrderPlacingScreenAction()

    data class GiftCertificateSelected(
        val cartType: CartType,
        val cart: Cart,
    ) : CheckoutOrderPlacingScreenAction()

    data class PaymentStarted(val paymentUrl: Url) : CheckoutOrderPlacingScreenAction()

    data class OrderConfirmed(val order: OrderDetails) : CheckoutOrderPlacingScreenAction()
}
