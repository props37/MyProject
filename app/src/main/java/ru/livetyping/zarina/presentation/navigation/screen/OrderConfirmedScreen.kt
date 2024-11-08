package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
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
                    CheckoutOrderConfirmedScreenAction.ReturnToHomeScreen -> {
                        navController.popBackStack<CheckoutGraph.OrderConfirmed>(inclusive = true)
                        navController.navigateToHomeScreen()
                    }

                    is CheckoutOrderConfirmedScreenAction.PaymentStarted -> {
                        val payment = UnscopedDestinations.Payment(action.paymentUrl.value)
                        val navOptions = navOptions {
                            popUpTo<CheckoutGraph.OrderConfirmed> { inclusive = true }
                        }
                        navController.navigate(payment, navOptions)
                    }
                }
            }
        )
    }
}
