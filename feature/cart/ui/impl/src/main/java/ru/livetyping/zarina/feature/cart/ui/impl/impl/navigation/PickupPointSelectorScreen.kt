package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.PickupPointSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.PickupPointSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.PickupPointSelectorScreen

internal fun NavGraphBuilder.pickupPointSelectorScreen(
    actions: PickupPointSelectorNavActions,
) {
    composable<PickupPointSelectorNavEntry>(
        typeMap = PickupPointSelectorNavEntry.typeMap(),
    ) {
        PickupPointSelectorScreen(actions)
    }
}
