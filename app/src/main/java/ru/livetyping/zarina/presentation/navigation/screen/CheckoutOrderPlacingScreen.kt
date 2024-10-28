package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import ru.livetyping.zarina.presentation.model.order.OrderDetailsParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingScreen
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingScreenAction

fun NavGraphBuilder.checkoutOrderPlacingScreen(navController: NavHostController) {
    composable<CheckoutGraph.OrderPlacing>(
        typeMap = CheckoutGraph.OrderPlacing.typeMap(),
    ) {
        CheckoutOrderPlacingScreen(
            navigate = { action ->
                when (action) {
                    CheckoutOrderPlacingScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.OrderPlacing>(inclusive = true)
                    }

                    CheckoutOrderPlacingScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutOrderPlacingScreenAction.ChangeCustomerClicked -> {
                        navController.popBackStack(
                            route = CheckoutGraph.Customer.routeSchema,
                            inclusive = false,
                        )
                    }

                    CheckoutOrderPlacingScreenAction.ChangeDeliveryClicked -> {
                        navController.popBackStack<CheckoutGraph.DeliveryMethod>(inclusive = false)
                    }

                    is CheckoutOrderPlacingScreenAction.PaymentStarted -> {
                        val payment = UnscopedDestinations.Payment(action.paymentUrl.value)
                        navController.navigate(payment)
                    }

                    is CheckoutOrderPlacingScreenAction.OrderConfirmed -> {
                        val orderParcelable = OrderDetailsParcelable.from(action.order)
                        val orderConfirmed = CheckoutGraph.OrderConfirmed(orderParcelable)
                        val navOptions = navOptions {
                            this.popUpTo(CheckoutGraph.routeSchema) { inclusive = true }
                        }
                        navController.navigate(orderConfirmed, navOptions)
                    }
                }
            },
        )
    }
}
