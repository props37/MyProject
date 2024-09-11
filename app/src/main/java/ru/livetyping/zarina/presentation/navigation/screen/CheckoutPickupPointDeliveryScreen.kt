package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutPickupPointDeliveryScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.PickupPointDelivery) {
        CheckoutPickupPointDeliveryScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPickupPointDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.PickupPointDelivery.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutPickupPointDeliveryScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutPickupPointDeliveryScreenAction.LocationPermissionRequired -> {
                        navController.navigateToPermissionRequirement(
                            permission = UnscopedDestinations.PermissionRequirement.Permission.LOCATION,
                            title = Text.Resource(R.string.grant_location_permission),
                            body = Text.Resource(R.string.it_will_help_us_to_detect_your_location),
                        )
                    }

                    is CheckoutPickupPointDeliveryScreenAction.PickupPointSelected -> {
                        navController.navigateToCheckoutSelectedPickupPointScreen(
                            cartType = action.cartType,
                            step = action.step,
                            deliveryMethodType = action.deliveryMethodType,
                            pickupPointId = action.pickupPoint.id,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutPickupPointDeliveryScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
) {
    val args = CheckoutGraph.PickupPointDelivery.Args(
        cartType = cartType,
        step = step,
        deliveryMethodType = deliveryMethodType,
    )
    this.navigate(
        route = CheckoutGraph.PickupPointDelivery.routeSchema,
        args = CheckoutGraph.PickupPointDelivery.createArgsBundle(args),
    )
}
