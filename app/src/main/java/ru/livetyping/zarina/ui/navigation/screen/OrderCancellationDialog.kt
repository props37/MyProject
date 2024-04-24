package ru.livetyping.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.ui.navigation.base.dialogDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.screen.order.cancellation.OrderCancellationScreen
import ru.livetyping.zarina.ui.screen.order.cancellation.OrderCancellationScreenAction
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
