package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams

sealed class SelectedPickupPointScreenAction {
    data object ScreenClosed : SelectedPickupPointScreenAction()

    data class ContinueClicked(
        val step: Int,
        val checkoutParams: PickupPointDeliveryCheckoutParams,
    ) : SelectedPickupPointScreenAction()
}
