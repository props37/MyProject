package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams

sealed class CheckoutSelectedPickupStoreScreenAction {
    data object ScreenClosed : CheckoutSelectedPickupStoreScreenAction()

    data class ContinueClicked(
        val cartType: CartType,
        val step: Int,
        val checkoutParams: StorePickupCheckoutParams,
    ) : CheckoutSelectedPickupStoreScreenAction()
}
