package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.customer.CheckoutCustomerScreen
import ru.livetyping.zarina.presentation.screen.checkout.customer.CheckoutCustomerScreenAction

fun NavGraphBuilder.checkoutCustomerScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.Customer) {
        CheckoutCustomerScreen(
            navigate = { action ->
                when (action) {
                    CheckoutCustomerScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutCustomerScreenAction.CustomerValidated -> {
                        when (action.cartType) {
                            CartType.DELIVERY -> {
                                val deliveryMethod = CheckoutGraph.DeliveryMethod(
                                    cartType = CartTypeParcelable.from(action.cartType),
                                    step = action.step,
                                    customer = CustomerParcelable.from(action.customer),
                                )
                                navController.navigate(deliveryMethod)
                            }

                            CartType.PICKUP -> {
                                val pickupStoreSelection = CheckoutGraph.PickupStoreSelection(
                                    cartType = CartTypeParcelable.from(action.cartType),
                                    step = action.step,
                                    deliveryMethodType = DeliveryMethodTypeParcelable.from(DeliveryMethodType.RETAIL),
                                    customer = CustomerParcelable.from(action.customer),
                                )
                                navController.navigate(pickupStoreSelection)
                            }
                        }
                    }
                }
            },
        )
    }
}
