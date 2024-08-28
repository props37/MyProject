package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.checkout.CourierDeliveryOptions
import ru.livetyping.zarina.presentation.model.checkout.CourierDeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorScreen
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorScreenAction
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CourierDeliveryDateTimeSelectorType
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.checkoutCourierDeliveryDateTimeSelectorScreen(
    navController: NavHostController,
) {
    composableDestination(CheckoutGraph.CourierDeliveryDateTimeSelector) {
        CheckoutCourierDeliveryDateTimeSelectorScreen(
            navigate = { action ->
                when (action) {
                    CheckoutCourierDeliveryDateTimeSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = CheckoutGraph.CourierDeliveryDateTimeSelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CheckoutCourierDeliveryDateTimeSelectorScreenAction.DateTimePeriodSelected -> {
                        navController.popBackStack(
                            route = CheckoutGraph.CourierDeliveryDateTimeSelector.routeSchema,
                            inclusive = true,
                        )
                        val result = CheckoutGraph.CourierDeliveryDateTimeSelector.Result(
                            deliveryOptionId = action.deliveryOptionId.value,
                            selectorType = action.selectorType,
                            dateTimePeriod = CourierDeliveryDateTimePeriodParcelable.from(action.dateTimePeriod),
                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = CheckoutGraph.CourierDeliveryDateTimeSelector.RESULT_KEY,
                            value = result,
                        )
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToCheckoutCourierDeliveryDateTimeSelectorScreen(
    type: CourierDeliveryDateTimeSelectorType,
    deliveryOptionId: CourierDeliveryOptions.Option.Id,
    dateTimePeriods: List<CourierDeliveryOptions.Option.DateTimePeriod>,
) {
    val args = CheckoutGraph.CourierDeliveryDateTimeSelector.Args(
        deliveryOptionId = deliveryOptionId,
        type = type,
        dateTimePeriods = dateTimePeriods,
    )
    this.navigate(
        route = CheckoutGraph.CourierDeliveryDateTimeSelector.routeSchema,
        args = CheckoutGraph.CourierDeliveryDateTimeSelector.createArgsBundle(args),
    )
}
