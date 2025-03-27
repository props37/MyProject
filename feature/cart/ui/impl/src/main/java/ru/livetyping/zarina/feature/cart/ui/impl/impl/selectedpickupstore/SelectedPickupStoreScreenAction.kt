package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams

internal sealed interface SelectedPickupStoreScreenAction {
    data object BackClicked : SelectedPickupStoreScreenAction

    data class ContinueClicked(
        val currentCheckoutStep: Int,
        val checkoutParams: PickupFromStoreCheckoutParams,
    ) : SelectedPickupStoreScreenAction
}
