package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreen
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutSelectedPickupPointScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.SelectedPickupPoint) {
        SelectedPickupPointScreen(
            navigate = { action ->
                when (action) {
                    SelectedPickupPointScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.SelectedPickupPoint.routeSchema,
                            inclusive = true,
                        )
                    }

                    is SelectedPickupPointScreenAction.ContinueClicked -> {
                        // TODO: [High] Implement
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutSelectedPickupPointScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
    pickupPointId: PickupPoint.Id,
) {
    val args = CheckoutGraph.SelectedPickupPoint.Args(
        cartType = cartType,
        step = step,
        deliveryMethodType = deliveryMethodType,
        pickupPointId = pickupPointId,
    )
    this.navigate(
        route = CheckoutGraph.SelectedPickupPoint.routeSchema,
        args = CheckoutGraph.SelectedPickupPoint.createArgsBundle(args),
    )
}
