package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientScreen
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientScreenAction

fun NavGraphBuilder.orderPlacementRecipientScreen(navController: NavHostController) {
    composableDestination(OrderPlacementGraph.Recipient) {
        OrderPlacementRecipientScreen(
            navigate = { action ->
                when (action) {
                    OrderPlacementRecipientScreenAction.OrderPlacementClosed -> {
                        navController.popBackStack(
                            route = OrderPlacementGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is OrderPlacementRecipientScreenAction.RecipientValidated -> {
                        navController.navigateToOrderPlacementStoreSelectionScreen(
                            cartType = action.cartType,
                            step = action.step,
                        )
                    }
                }
            },
        )
    }
}
