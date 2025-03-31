package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class PickupPointSelectorNavActions(
    val onBackClicked: () -> Unit,
    val onCloseClicked: () -> Unit,
    val onPickupPointSelected: (
        cartType: CartType,
        currentCheckoutStep: Int,
        recipient: Recipient,
        deliveryMethod: DeliveryMethod,
        pickupPoint: PickupPoint,
    ) -> Unit,
) : NavigationActions
