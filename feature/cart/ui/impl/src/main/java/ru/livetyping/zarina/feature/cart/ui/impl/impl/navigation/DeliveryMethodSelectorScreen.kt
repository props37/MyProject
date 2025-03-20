package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorScreen

internal fun NavGraphBuilder.deliveryMethodSelectorScreen(
    actions: DeliveryMethodSelectorNavActions,
) {
    composable<DeliveryMethodSelectorNavEntry>(
        typeMap = DeliveryMethodSelectorNavEntry.typeMap(),
    ) {
        DeliveryMethodSelectorScreen(actions)
    }
}
