package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.model.checkout.DeliveryOptionParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryScreen
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryScreenAction
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CourierDeliveryDateTimeSelectorType

fun NavGraphBuilder.checkoutCourierDeliveryScreen(navController: NavHostController) {
    composable<CheckoutGraph.CourierDelivery>(
        typeMap = CheckoutGraph.CourierDelivery.typeMap(),
    ) {
        CheckoutCourierDeliveryScreen(
            viewModel = hiltViewModel { factory: CheckoutCourierDeliveryViewModel.Factory ->
                val dateTimePeriodSelectorResultFlow = it.savedStateHandle
                    .getStateFlow<CheckoutGraph.CourierDeliveryDateTimeSelector.Result?>(
                        key = CheckoutGraph.CourierDeliveryDateTimeSelector.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(dateTimePeriodSelectorResultFlow)
            },
            navigate = { action ->
                when (action) {
                    CheckoutCourierDeliveryScreenAction.ScreenClosed -> {
                        navController.popBackStack<CheckoutGraph.CourierDelivery>(
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
                        val courierDeliveryDateTimeSelector = CheckoutGraph.CourierDeliveryDateTimeSelector(
                            type = CourierDeliveryDateTimeSelectorType.DATE,
                            deliveryOptionId = action.deliveryOptionId.value,
                            dateTimePeriods = action.dateTimePeriods.map {
                                DeliveryOptionParcelable.DateTimePeriod.from(it)
                            },
                        )
                        navController.navigate(courierDeliveryDateTimeSelector)
                    }

                    is CheckoutCourierDeliveryScreenAction.DeliveryTimeClicked -> {
                        val courierDeliveryDateTimeSelector = CheckoutGraph.CourierDeliveryDateTimeSelector(
                            type = CourierDeliveryDateTimeSelectorType.TIME,
                            deliveryOptionId = action.deliveryOptionId.value,
                            dateTimePeriods = action.dateTimePeriods.map {
                                DeliveryOptionParcelable.DateTimePeriod.from(it)
                            },
                        )
                        navController.navigate(courierDeliveryDateTimeSelector)
                    }

                    is CheckoutCourierDeliveryScreenAction.ContinueClicked -> {
                        val orderPlacing = CheckoutGraph.OrderPlacing(
                            step = action.step,
                            checkoutParams = CheckoutParamsParcelable.from(action.checkoutParams),
                        )
                        navController.navigate(orderPlacing)
                    }
                }
            },
        )
    }
}
