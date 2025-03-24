package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.PickupStoreSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.PickupStoreSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.PickupStoreSelectorScreen

internal fun NavGraphBuilder.pickupStoreSelectorScreen(
    actions: PickupStoreSelectorNavActions,
) {
    composable<PickupStoreSelectorNavEntry>(
        typeMap = PickupStoreSelectorNavEntry.typeMap(),
    ) {
        PickupStoreSelectorScreen(actions)
    }
}
