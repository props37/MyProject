package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorScreen

internal fun NavGraphBuilder.deliveryOptionDateTimeSelectorScreen(
    actions: DeliveryOptionDateTimeSelectorNavActions,
) {
    composable<DeliveryOptionDateTimeSelectorNavEntry>(
        typeMap = DeliveryOptionDateTimeSelectorNavEntry.typeMap(),
    ) {
        DeliveryOptionDateTimeSelectorScreen(actions)
    }
}
