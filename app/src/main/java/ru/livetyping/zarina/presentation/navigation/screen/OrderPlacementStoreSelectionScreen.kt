package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutStoreSelectionScreen
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutStoreSelectionScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutStoreSelectionScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.StoreSelection) {
        CheckoutStoreSelectionScreen(
            navigate = { action ->
                when (action) {
                    CheckoutStoreSelectionScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.StoreSelection.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutStoreSelectionScreenAction.CheckoutClosed -> {
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

fun NavHostController.navigateToCheckoutStoreSelectionScreen(cartType: CartType, step: Int) {
    val args = CheckoutGraph.StoreSelection.Args(cartType, step)
    this.navigate(
        route = CheckoutGraph.StoreSelection.routeSchema,
        args = CheckoutGraph.StoreSelection.createArgsBundle(args),
    )
}
