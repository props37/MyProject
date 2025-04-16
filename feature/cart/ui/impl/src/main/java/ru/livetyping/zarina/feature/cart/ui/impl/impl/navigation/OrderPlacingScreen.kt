package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.feature.cart.ui.api.PaymentResult
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.GiftCertificateScreenResult
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel

internal fun NavGraphBuilder.orderPlacingScreen(
    actions: OrderPlacingNavActions,
    paymentResultRetriever: ScreenResultRetriever<PaymentResult>,
) {
    composable<OrderPlacingNavEntry>(typeMap = OrderPlacingNavEntry.typeMap()) { navBackStackEntry ->
        OrderPlacingScreen(
            navActions = actions,
            viewModel =  hiltViewModel { factory: OrderPlacingViewModel.Factory ->
                val giftCertificateResultFlow = navBackStackEntry.savedStateHandle
                    .getStateFlow(GiftCertificateScreenResult.KEY, initialValue = null)
                val paymentResultFlow = paymentResultRetriever.get(navBackStackEntry)
                factory.create(
                    giftCertificateResultFlow = giftCertificateResultFlow,
                    paymentResultFlow = paymentResultFlow,
                )
            },
        )
    }
}
