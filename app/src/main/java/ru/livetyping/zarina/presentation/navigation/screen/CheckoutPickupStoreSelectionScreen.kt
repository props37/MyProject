package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
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
                        val selectedPickupStore = CheckoutGraph.SelectedPickupStore(
                            cartType = CartTypeParcelable.from(action.cartType),
                            step = action.step,
                            deliveryMethodType = DeliveryMethodTypeParcelable.from(action.deliveryMethodType),
                            city = CityParcelable.from(action.city),
                            store = StoreParcelable.from(action.store),
                            availableProducts = action.availableProducts.map {
                                CartProductParcelable.from(it)
                            },
                            customer = CustomerParcelable.from(action.customer),
                        )
                        navController.navigate(selectedPickupStore)
                    }
                }
            },
        )
    }
}
