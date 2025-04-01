package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model

import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed

internal sealed interface SelectedPickupPointEvent {
    data object BackClicked : SelectedPickupPointEvent

    data class DeliveryTypeClicked(
        val deliveryType: PickupPointDetailed.DeliveryType,
    ) : SelectedPickupPointEvent

    data object ContinueClicked : SelectedPickupPointEvent

    data object ErrorRefreshClicked : SelectedPickupPointEvent
}
