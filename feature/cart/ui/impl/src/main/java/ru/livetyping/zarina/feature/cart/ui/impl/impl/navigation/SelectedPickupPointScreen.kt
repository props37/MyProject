package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.SelectedPickupPointNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.SelectedPickupPointNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.SelectedPickupPointScreen

internal fun NavGraphBuilder.selectedPickupPointScreen(
    actions: SelectedPickupPointNavActions,
) {
    composable<SelectedPickupPointNavEntry>(
        typeMap = SelectedPickupPointNavEntry.typeMap(),
    ) {
        SelectedPickupPointScreen(actions)
    }
}
