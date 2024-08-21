package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreen
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutDeliveryMethodScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.DeliveryMethod) {
        CheckoutDeliveryMethodScreen(
            navigate = { action ->
                when (action) {
                    CheckoutDeliveryMethodScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.DeliveryMethod.routeSchema,
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
                        when (action.method.type) {
                            DeliveryMethodType.EXPRESS -> {
                                navController.navigateToCheckoutCourierDeliveryScreen(
                                    cartType = action.cartType,
                                    step = action.step,
                                    deliveryMethodType = action.method.type,
                                )
                            }

                            DeliveryMethodType.POST -> Unit // TODO: [High] Implement
                            DeliveryMethodType.PICKUP -> Unit // TODO: [High] Implement
                            else -> Unit
                        }
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutDeliveryMethodScreen(
    cartType: CartType,
    step: Int,
) {
    val args = CheckoutGraph.DeliveryMethod.Args(
        cartType = cartType,
        step = step,
    )
    this.navigate(
        route = CheckoutGraph.DeliveryMethod.routeSchema,
        args = CheckoutGraph.DeliveryMethod.createArgsBundle(args),
    )
}
