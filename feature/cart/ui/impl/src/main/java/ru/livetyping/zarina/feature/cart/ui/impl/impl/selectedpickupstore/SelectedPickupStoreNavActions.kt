package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SelectedPickupStoreNavActions(
    val onBackClicked: () -> Unit,
    val onContinueClicked: (
        currentCheckoutStep: Int,
        checkoutParams: PickupFromStoreCheckoutParams,
    ) -> Unit,
) : NavigationActions
