package ru.livetyping.zarina.presentation.screen.checkout.storeselection

import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.store.Store

sealed class CheckoutStoreSelectionScreenAction {
    data object ScreenClosed : CheckoutStoreSelectionScreenAction()

    data object CheckoutClosed : CheckoutStoreSelectionScreenAction()

    data class StoreClicked(
        val cartType: CartType,
        val step: Int,
        val store: Store,
        val availableProducts: List<CartProduct>,
    ) : CheckoutStoreSelectionScreenAction()
}
