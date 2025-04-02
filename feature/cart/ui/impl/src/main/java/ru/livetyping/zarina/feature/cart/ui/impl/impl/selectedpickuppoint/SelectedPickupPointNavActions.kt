package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint

import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams

internal class SelectedPickupPointNavActions(
    val onBackClicked: () -> Unit,
    val onContinueClicked: (
        currentCheckoutStep: Int,
        checkoutParams: PickupFromPickupPointCheckoutParams,
    ) -> Unit,
)
