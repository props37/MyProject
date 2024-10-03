package ru.livetyping.zarina.presentation.screen.checkout.pickupstoreselection

import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.store.Store

sealed class CheckoutPickupStoreSelectionScreenAction {
    data object ScreenClosed : CheckoutPickupStoreSelectionScreenAction()

    data object CheckoutClosed : CheckoutPickupStoreSelectionScreenAction()

    data class StoreClicked(
        val cartType: CartType,
        val step: Int,
        val deliveryMethodType: DeliveryMethodType,
        val city: City,
        val store: Store,
        val availableProducts: List<CartProduct>,
        val customer: Customer,
    ) : CheckoutPickupStoreSelectionScreenAction()
}
