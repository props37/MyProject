package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.order.OrderScreen
import ru.livetyping.zarina.presentation.screen.order.OrderScreenAction
import ru.livetyping.zarina.presentation.screen.order.OrderViewModel
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.orderScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.Order,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.MyOrders.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.MyOrders.routeSchema -> slidePopExitTransition()
                else -> null
            }
        }
    ) {
        OrderScreen(
            viewModel = hiltViewModel { factory: OrderViewModel.Factory ->
                val orderCancellationResultFlow = it.savedStateHandle
                    .getStateFlow<ProfileGraph.OrderCancellation.Result?>(
                        key = ProfileGraph.OrderCancellation.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(orderCancellationResultFlow)
            },
            navigate = { action ->
                when (action) {
                    OrderScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.Order.routeSchema,
                            inclusive = true,
                        )
                    }

                    is OrderScreenAction.CancelOrderClicked -> {
                        navController.navigateToOrderCancellationDialog(action.orderId)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToOrderScreen(orderId: Order.Id) {
    val args = ProfileGraph.Order.Args(orderId)
    this.navigate(
        route = ProfileGraph.Order.routeSchema,
        args = ProfileGraph.Order.createArgsBundle(args),
    )
}
