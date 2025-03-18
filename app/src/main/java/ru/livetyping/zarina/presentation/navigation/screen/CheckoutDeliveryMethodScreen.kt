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
                        val cartType = CartTypeParcelable.from(action.cartType)
                        val step = action.step
                        val customer = CustomerParcelable.from(action.customer)
                        val deliveryMethodType = DeliveryMethodTypeParcelable.from(action.method.type)
                        when (action.method.type) {
                            DeliveryMethodType.EXPRESS -> {
                                val courierDelivery = CheckoutGraph.CourierDelivery(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                    customer = customer,
                                )
                                navController.navigate(courierDelivery)
                            }

                            DeliveryMethodType.POST -> {
                                val postDelivery = CheckoutGraph.PostDelivery(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                    customer = customer,
                                )
                                navController.navigate(postDelivery)
                            }

                            DeliveryMethodType.PICKUP -> {
                                val pickupPointDelivery = CheckoutGraph.PickupPointDelivery(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                    customer = customer,
                                )
                                navController.navigate(pickupPointDelivery)
                            }

                            DeliveryMethodType.RETAIL, DeliveryMethodType.PICKUP_IN_STORE -> {
                                val pickupStoreSelection = CheckoutGraph.PickupStoreSelection(
                                    cartType = cartType,
                                    step = step,
                                    deliveryMethodType = deliveryMethodType,
                                    customer = customer,
                                )
                                navController.navigate(pickupStoreSelection)
                            }

                            else -> Unit
                        }
                    }
                }
            },
        )
    }
}
