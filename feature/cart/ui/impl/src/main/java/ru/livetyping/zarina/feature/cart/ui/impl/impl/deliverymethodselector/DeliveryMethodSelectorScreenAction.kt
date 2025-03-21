package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient

internal sealed interface DeliveryMethodSelectorScreenAction {
    data object BackClicked : DeliveryMethodSelectorScreenAction

    data object CloseClicked : DeliveryMethodSelectorScreenAction

    data class DeliveryMethodSelected(
        val cartType: CartType,
        val currentCheckoutStep: Int,
        val recipient: Recipient,
        val deliveryMethod: DeliveryMethod,
    ) : DeliveryMethodSelectorScreenAction
}
