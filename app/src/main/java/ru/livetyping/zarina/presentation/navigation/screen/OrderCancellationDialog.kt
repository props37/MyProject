package ru.livetyping.zarina.presentation.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.presentation.navigation.base.dialogDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.screen.order.cancellation.OrderCancellationScreen
import ru.livetyping.zarina.presentation.screen.order.cancellation.OrderCancellationScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.orderCancellationDialog(navController: NavHostController) {
    dialogDestination(
        destination = ProfileGraph.OrderCancellation,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        OrderCancellationScreen(
            navigate = { action ->
                when (action) {
                    OrderCancellationScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.OrderCancellation.routeSchema,
                            inclusive = true,
                        )
                    }

                    OrderCancellationScreenAction.OrderCancelled -> {
                        navController.popBackStack(
                            route = ProfileGraph.OrderCancellation.routeSchema,
                            inclusive = true,
                        )
                        val result = ProfileGraph.OrderCancellation.Result()
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(ProfileGraph.OrderCancellation.RESULT_KEY, result)
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToOrderCancellationDialog(orderId: Order.Id) {
    val args = ProfileGraph.OrderCancellation.Args(orderId)
    this.navigate(
        route = ProfileGraph.OrderCancellation.routeSchema,
        args = ProfileGraph.OrderCancellation.createArgsBundle(args),
    )
}
