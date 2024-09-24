package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.pickupstoreselection.CheckoutPickupStoreSelectionScreen
import ru.livetyping.zarina.presentation.screen.checkout.pickupstoreselection.CheckoutPickupStoreSelectionScreenAction

fun NavGraphBuilder.checkoutPickupStoreSelectionScreen(navController: NavHostController) {
    composable<CheckoutGraph.PickupStoreSelection>(
        typeMap = CheckoutGraph.PickupStoreSelection.typeMap(),
    ) {
        CheckoutPickupStoreSelectionScreen(
            navigate = { action ->
                when (action) {
                    CheckoutPickupStoreSelectionScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.PickupStoreSelection>(
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
