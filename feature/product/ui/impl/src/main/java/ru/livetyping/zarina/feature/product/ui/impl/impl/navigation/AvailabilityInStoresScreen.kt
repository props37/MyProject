package ru.livetyping.zarina.feature.product.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavEntry
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresScreen

internal fun NavGraphBuilder.availabilityInStoresScreen(actions: AvailabilityInStoresNavActions) {
    composable<AvailabilityInStoresNavEntry>(
        typeMap = AvailabilityInStoresNavEntry.typeMap(),
    ) {
        AvailabilityInStoresScreen(actions)
    }
}
