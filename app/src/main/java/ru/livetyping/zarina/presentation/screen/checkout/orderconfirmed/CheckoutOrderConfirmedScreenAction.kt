package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import ru.livetyping.zarina.domain.common.Url

sealed class CheckoutOrderConfirmedScreenAction {
    data object ReturnToHomeScreen : CheckoutOrderConfirmedScreenAction()

    data class PaymentStarted(val paymentUrl: Url) : CheckoutOrderConfirmedScreenAction()
}
