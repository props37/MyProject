package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams

sealed class CheckoutSelectedPickupStoreScreenAction {
    data object ScreenClosed : CheckoutSelectedPickupStoreScreenAction()

    data class ContinueClicked(
        val step: Int,
        val checkoutParams: StorePickupCheckoutParams,
    ) : CheckoutSelectedPickupStoreScreenAction()
}
