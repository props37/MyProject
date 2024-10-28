package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

sealed class CheckoutOrderConfirmedScreenAction {
    data object ReturnToHomeScreen : CheckoutOrderConfirmedScreenAction()
}
