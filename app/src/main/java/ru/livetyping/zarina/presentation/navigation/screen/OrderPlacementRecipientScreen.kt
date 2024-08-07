package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientScreen
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientScreenAction

fun NavGraphBuilder.orderPlacementRecipientScreen(navController: NavHostController) {
    composableDestination(
        destination = OrderPlacementGraph.Recipient,
        enterTransition = {
            when (initialState.destination.route) {
                CartGraph.Cart.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                CartGraph.Cart.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        OrderPlacementRecipientScreen(
            navigate = { action ->
                when (action) {
                    OrderPlacementRecipientScreenAction.OrderPlacementClosed -> {
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
