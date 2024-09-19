package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.pickupstoreselection.CheckoutPickupStoreSelectionScreen
import ru.livetyping.zarina.presentation.screen.checkout.pickupstoreselection.CheckoutPickupStoreSelectionScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutPickupStoreSelectionScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.PickupStoreSelection) {
        CheckoutPickupStoreSelectionScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPickupStoreSelectionScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.PickupStoreSelection.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutPickupStoreSelectionScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutPickupStoreSelectionScreenAction.StoreClicked -> {
                        navController.navigateToCheckoutPickupSelectedStoreScreen(
                            cartType = action.cartType,
                            step = action.step,
                            deliveryMethodType = action.deliveryMethodType,
                            city = action.city,
                            store = action.store,
                            availableProducts = action.availableProducts,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutPickupStoreSelectionScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
) {
    val args = CheckoutGraph.PickupStoreSelection.Args(cartType, step, deliveryMethodType)
    this.navigate(
        route = CheckoutGraph.PickupStoreSelection.routeSchema,
        args = CheckoutGraph.PickupStoreSelection.createArgsBundle(args),
    )
}
