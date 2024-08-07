package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.navigation.screen.orderPlacementRecipientScreen
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.orderPlacementGraph(navController: NavHostController) {
    navigationGraph(OrderPlacementGraph) {
        orderPlacementRecipientScreen(navController)
    }
}

fun NavHostController.navigateToOrderPlacementGraph(cartType: CartType) {
    val args = OrderPlacementGraph.Recipient.Args(cartType)
    this.navigate(
        route = OrderPlacementGraph.routeSchema,
        args = OrderPlacementGraph.createArgsBundle(args),
    )
}
