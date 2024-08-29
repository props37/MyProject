package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.postdelivery.CheckoutPostDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.postdelivery.CheckoutPostDeliveryScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutPostDeliveryScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.PostDelivery) {
        CheckoutPostDeliveryScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPostDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.PostDelivery.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutPostDeliveryScreenAction.CheckoutClosed -> {
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

fun NavHostController.navigateToCheckoutPostDeliveryScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
) {
    val args = CheckoutGraph.PostDelivery.Args(
        cartType = cartType,
        step = step,
        deliveryMethodType = deliveryMethodType,
    )
    this.navigate(
        route = CheckoutGraph.PostDelivery.routeSchema,
        args = CheckoutGraph.PostDelivery.createArgsBundle(args),
    )
}
