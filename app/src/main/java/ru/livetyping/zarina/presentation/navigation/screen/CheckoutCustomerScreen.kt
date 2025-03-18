package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.customer.CheckoutCustomerScreen
import ru.livetyping.zarina.presentation.screen.checkout.customer.CheckoutCustomerScreenAction

fun NavGraphBuilder.checkoutCustomerScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.Customer) {
        CheckoutCustomerScreen(
            navigate = { action ->
                when (action) {
                    CheckoutCustomerScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutCustomerScreenAction.CustomerValidated -> {
                        val deliveryMethod = CheckoutGraph.DeliveryMethod(
                            cartType = CartTypeParcelable.from(action.cartType),
                            step = action.step,
                            customer = CustomerParcelable.from(action.customer),
                        )
                        navController.navigate(deliveryMethod)
                    }
                }
            },
        )
    }
}
