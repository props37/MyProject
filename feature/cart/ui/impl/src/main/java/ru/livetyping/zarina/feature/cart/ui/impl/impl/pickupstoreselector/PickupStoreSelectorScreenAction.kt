package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.store.Store

internal sealed interface PickupStoreSelectorScreenAction {
    data object BackClicked : PickupStoreSelectorScreenAction

    data object CloseClicked : PickupStoreSelectorScreenAction

    data class StoreSelected(
        val cartType: CartType,
        val currentCheckoutStep: Int,
        val recipient: Recipient,
        val deliveryMethodType: DeliveryMethodType,
        val city: City,
        val store: Store,
        val availableProducts: List<CartProduct>,
    ) : PickupStoreSelectorScreenAction
}
