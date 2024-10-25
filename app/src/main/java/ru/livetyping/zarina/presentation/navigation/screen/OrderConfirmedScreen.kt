package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedScreen
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedScreenAction

fun NavGraphBuilder.checkoutOrderConfirmedScreen(navController: NavHostController) {
    composable<CheckoutGraph.OrderConfirmed>(
        typeMap = CheckoutGraph.OrderConfirmed.typeMap(),
    ) {
        CheckoutOrderConfirmedScreen(
            navigate = { action ->
                when (action) {
                    CheckoutOrderConfirmedScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.OrderConfirmed>(inclusive = true)
                    }
                }
            }
        )
    }
}
