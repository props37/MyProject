package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.DeliveryAddressSelectorViewModel
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorScreenResult

internal fun NavGraphBuilder.deliveryAddressSelectorScreen(
    actions: DeliveryAddressSelectorNavActions,
) {
    composable<DeliveryAddressSelectorNavEntry>(
        typeMap = DeliveryAddressSelectorNavEntry.typeMap(),
    ) { navBackStackEntry ->
        DeliveryAddressSelectorScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: DeliveryAddressSelectorViewModel.Factory ->
                val deliveryOptionDateTimeSelectorResult = navBackStackEntry.savedStateHandle
                    .getStateFlow<DeliveryOptionDateTimeSelectorScreenResult?>(
                        key = DeliveryOptionDateTimeSelectorScreenResult.KEY,
                        initialValue = null,
                    )
                factory.create(deliveryOptionDateTimeSelectorResult)
            },
        )
    }
}
