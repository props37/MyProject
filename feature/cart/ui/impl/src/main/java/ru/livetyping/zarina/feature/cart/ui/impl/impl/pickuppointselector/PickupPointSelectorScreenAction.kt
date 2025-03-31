package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.Recipient

internal sealed interface PickupPointSelectorScreenAction {
    data object BackClicked : PickupPointSelectorScreenAction

    data object CloseClicked : PickupPointSelectorScreenAction

    data class PickupPointSelected(
        val cartType: CartType,
        val currentCheckoutStep: Int,
        val recipient: Recipient,
        val deliveryMethod: DeliveryMethod,
        val pickupPoint: PickupPoint,
    ) : PickupPointSelectorScreenAction
}
