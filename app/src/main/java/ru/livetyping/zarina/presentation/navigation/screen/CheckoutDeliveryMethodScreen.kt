package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreen
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreenAction

fun NavGraphBuilder.checkoutDeliveryMethodScreen(navController: NavHostController) {
    composable<CheckoutGraph.DeliveryMethod>(
        typeMap = CheckoutGraph.DeliveryMethod.typeMap(),
    ) {
        CheckoutDeliveryMethodScreen(
            navigate = { action ->
                when (action) {
                    CheckoutDeliveryMethodScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.DeliveryMethod>(
                            inclusive = true,
                        )
                    }

                    CheckoutDeliveryMethodScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutDeliveryMethodScreenAction.DeliveryMethodSelected -> {
                        val cartType = action.cartType
                        val step = action.step
                        when (val deliveryMethodType = action.method.type) {
                            DeliveryMethodType.EXPRESS -> {
                                val courierDelivery = CheckoutGraph.CourierDelivery(
                                    cartType = CartTypeParcelable.from(action.cartType),
                                    step = action.step,
                                    deliveryMethodType = DeliveryMethodTypeParcelable.from(deliveryMethodType),
                                    customer = CustomerParcelable.from(action.customer),
                                )
                                navController.navigate(courierDelivery)
                            }

                            DeliveryMethodType.POST -> {
                                navController.navigateToCheckoutPostDeliveryScreen(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                )
                            }

                            DeliveryMethodType.PICKUP -> {
                                navController.navigateToCheckoutPickupPointDeliveryScreen(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                )
                            }

                            else -> Unit
                        }
                    }
                }
            },
        )
    }
}
