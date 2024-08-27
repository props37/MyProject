package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryScreenAction
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CourierDeliveryDateTimeSelectorType
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutCourierDeliveryScreen(navController: NavHostController) {
    composableDestination(CheckoutGraph.CourierDelivery) {
        CheckoutCourierDeliveryScreen(
            navigate = { action ->
                when (action) {
                    CheckoutCourierDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.CourierDelivery.routeSchema,
                            inclusive = true,
                        )
                    }

                    CheckoutCourierDeliveryScreenAction.CheckoutClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutCourierDeliveryScreenAction.DeliveryDateClicked -> {
                        navController.navigateToCheckoutCourierDeliveryDateTimeSelectorScreen(
                            type = CourierDeliveryDateTimeSelectorType.DATE,
                            dateTimePeriods = action.dateTimePeriods,
                        )
                    }

                    is CheckoutCourierDeliveryScreenAction.DeliveryTimeClicked -> {
                        navController.navigateToCheckoutCourierDeliveryDateTimeSelectorScreen(
                            type = CourierDeliveryDateTimeSelectorType.TIME,
                            dateTimePeriods = action.dateTimePeriods,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCheckoutCourierDeliveryScreen(
    cartType: CartType,
    step: Int,
    deliveryMethodType: DeliveryMethodType,
) {
    val args = CheckoutGraph.CourierDelivery.Args(
        cartType = cartType,
        step = step,
        deliveryMethodType = deliveryMethodType,
    )
    this.navigate(
        route = CheckoutGraph.CourierDelivery.routeSchema,
        args = CheckoutGraph.CourierDelivery.createArgsBundle(args),
    )
}
