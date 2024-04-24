package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.order.OrderScreen
import ru.livetyping.zarina.ui.screen.order.OrderScreenAction
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
