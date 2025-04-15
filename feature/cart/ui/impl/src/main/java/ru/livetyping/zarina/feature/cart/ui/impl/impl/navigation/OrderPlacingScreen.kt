package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel

internal fun NavGraphBuilder.orderPlacingScreen(actions: OrderPlacingNavActions) {
    composable<OrderPlacingNavEntry>(typeMap = OrderPlacingNavEntry.typeMap()) {
        OrderPlacingScreen(
            navActions = actions,
            viewModel =  hiltViewModel { factory: OrderPlacingViewModel.Factory ->
                // TODO: [Top] Implement
                factory.create(
                    giftCertificateResultFlow = MutableStateFlow(null),
                    paymentResultFlow = MutableStateFlow(null),
                )
            },
        )
    }
}
