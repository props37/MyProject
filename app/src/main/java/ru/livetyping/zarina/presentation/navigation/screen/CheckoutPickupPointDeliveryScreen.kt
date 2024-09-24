package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryScreenAction

fun NavGraphBuilder.checkoutPickupPointDeliveryScreen(navController: NavHostController) {
    composable<CheckoutGraph.PickupPointDelivery>(
        typeMap = CheckoutGraph.PickupPointDelivery.typeMap(),
    ) {
        CheckoutPickupPointDeliveryScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPickupPointDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.PickupPointDelivery>(
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
