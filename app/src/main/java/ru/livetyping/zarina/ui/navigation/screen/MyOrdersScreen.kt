package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slideExitTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersScreen
import ru.livetyping.zarina.ui.screen.myorders.MyOrdersScreenAction

fun NavGraphBuilder.myOrdersScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.MyOrders,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.Order.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Order.routeSchema -> slidePopEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slidePopExitTransition()
                else -> null
            }
        }
    ) {
        MyOrdersScreen(
            navigate = { action ->
                when (action) {
                    MyOrdersScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.MyOrders.routeSchema,
                            inclusive = true,
                        )
                    }

                    is MyOrdersScreenAction.OrderClicked -> {
                        navController.navigateToOrderScreen(action.orderId)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToMyOrdersScreen() {
    this.navigate(ProfileGraph.MyOrders.route)
}
