package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedstore.CheckoutSelectedStoreScreen
import ru.livetyping.zarina.presentation.screen.checkout.selectedstore.CheckoutSelectedStoreScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutSelectedStoreScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.SelectedStore) {
        CheckoutSelectedStoreScreen(
            navigate = { action ->
                when (action) {
                    CheckoutSelectedStoreScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.SelectedStore.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutSelectedStoreScreen(
    cartType: CartType,
    step: Int,
    store: Store,
    availableProducts: List<CartProduct>,
) {
    val args = CheckoutGraph.SelectedStore.Args(
        cartType = cartType,
        step = step,
        store = store,
        availableProducts = availableProducts,
    )
    this.navigate(
        route = CheckoutGraph.SelectedStore.routeSchema,
        args = CheckoutGraph.SelectedStore.createArgsBundle(args),
    )
}
