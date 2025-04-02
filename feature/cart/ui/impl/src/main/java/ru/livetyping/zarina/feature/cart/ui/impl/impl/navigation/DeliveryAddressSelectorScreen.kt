package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.CourierDeliverySelectorScreen

internal fun NavGraphBuilder.deliveryAddressSelectorScreen(
    actions: DeliveryAddressSelectorNavActions,
) {
    composable<DeliveryAddressSelectorNavEntry>(
        typeMap = DeliveryAddressSelectorNavEntry.typeMap(),
    ) {
        CourierDeliverySelectorScreen(actions)
    }
}
