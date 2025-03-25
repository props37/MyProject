package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class PickupStoreSelectorNavActions(
    val onBackClicked: () -> Unit,
    val onCloseClicked: () -> Unit,
    val onStoreSelected: (
        cartType: CartType,
        currentCheckoutStep: Int,
        recipient: Recipient,
        deliveryMethodType: DeliveryMethodType,
        city: City,
        store: Store,
        availableProducts: List<CartProduct>,
    ) -> Unit,
) : NavigationActions
