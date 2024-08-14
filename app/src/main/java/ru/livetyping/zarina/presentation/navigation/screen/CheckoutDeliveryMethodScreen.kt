package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreen
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutDeliveryMethodScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.DeliveryMethod) {
        CheckoutDeliveryMethodScreen(
            navigate = { action ->
                when (action) {
                    CheckoutDeliveryMethodScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.DeliveryMethod.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutDeliveryMethodScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutDeliveryMethodScreen(
    cartType: CartType,
    step: Int,
) {
    val args = CheckoutGraph.DeliveryMethod.Args(
        cartType = cartType,
        step = step,
    )
    this.navigate(
        route = CheckoutGraph.DeliveryMethod.routeSchema,
        args = CheckoutGraph.DeliveryMethod.createArgsBundle(args),
    )
}
