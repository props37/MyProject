package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.SelectedPickupStoreNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.SelectedPickupStoreNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.SelectedPickupStoreScreen

internal fun NavGraphBuilder.selectedPickupStoreScreen(
    actions: SelectedPickupStoreNavActions,
) {
    composable<SelectedPickupStoreNavEntry>(
        typeMap = SelectedPickupStoreNavEntry.typeMap(),
    ) {
        SelectedPickupStoreScreen(actions)
    }
}
