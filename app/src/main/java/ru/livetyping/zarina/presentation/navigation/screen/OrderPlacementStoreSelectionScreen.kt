package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.screen.orderplacement.storeselection.OrderPlacementStoreSelectionScreen
import ru.livetyping.zarina.presentation.screen.orderplacement.storeselection.OrderPlacementStoreSelectionScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.orderPlacementStoreSelectionScreen(navController: NavHostController) {
    composableDestination(OrderPlacementGraph.StoreSelection) {
        OrderPlacementStoreSelectionScreen(
            navigate = { action ->
                when (action) {
                    OrderPlacementStoreSelectionScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = OrderPlacementGraph.StoreSelection.routeSchema,
                            inclusive = true,
                        )
                    }

                    OrderPlacementStoreSelectionScreenAction.OrderPlacementClosed -> {
                        navController.popBackStack(
                            route = OrderPlacementGraph.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToOrderPlacementStoreSelectionScreen(cartType: CartType, step: Int) {
    val args = OrderPlacementGraph.StoreSelection.Args(cartType, step)
    this.navigate(
        route = OrderPlacementGraph.StoreSelection.routeSchema,
        args = OrderPlacementGraph.StoreSelection.createArgsBundle(args),
    )
}
