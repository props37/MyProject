package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreScreen
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutSelectedPickupStoreScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.SelectedPickupStore) {
        CheckoutSelectedPickupStoreScreen(
            navigate = { action ->
                when (action) {
                    CheckoutSelectedPickupStoreScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.SelectedPickupStore.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutSelectedPickupStoreScreenAction.ContinueClicked -> {
                        // TODO: [High] Implement
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutPickupSelectedStoreScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
    city: City,
    store: Store,
    availableProducts: List<CartProduct>,
) {
    val args = CheckoutGraph.SelectedPickupStore.Args(
        cartType = cartType,
        step = step,
        deliveryMethodType = deliveryMethodType,
        city = city,
        store = store,
        availableProducts = availableProducts,
    )
    this.navigate(
        route = CheckoutGraph.SelectedPickupStore.routeSchema,
        args = CheckoutGraph.SelectedPickupStore.createArgsBundle(args),
    )
}
