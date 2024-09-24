package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorScreen
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorScreenAction

fun NavGraphBuilder.checkoutCourierDeliveryDateTimeSelectorScreen(
    navController: NavHostController,
) {
    composable<CheckoutGraph.CourierDeliveryDateTimeSelector>(
        typeMap = CheckoutGraph.CourierDeliveryDateTimeSelector.typeMap(),
    ) {
        CheckoutCourierDeliveryDateTimeSelectorScreen(
            navigate = { action ->
                when (action) {
                    CheckoutCourierDeliveryDateTimeSelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.CourierDeliveryDateTimeSelector>(
                            inclusive = true,
                        )
                    }

                    is CheckoutCourierDeliveryDateTimeSelectorScreenAction.DateTimePeriodSelected -> {
                        navController.popBackStack<CheckoutGraph.CourierDeliveryDateTimeSelector>(
                            inclusive = true,
                        )
                        val result = CheckoutGraph.CourierDeliveryDateTimeSelector.Result(
                            deliveryOptionId = action.deliveryOptionId.value,
                            selectorType = action.selectorType,
                            dateTimePeriod = DeliveryDateTimePeriodParcelable.from(action.dateTimePeriod),
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
