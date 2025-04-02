package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint

import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams

internal sealed interface SelectedPickupPointScreenAction {
    data object BackClicked : SelectedPickupPointScreenAction

    data class ContinueClicked(
        val currentCheckoutStep: Int,
        val checkoutParams: PickupFromPickupPointCheckoutParams,
    ) : SelectedPickupPointScreenAction
}
