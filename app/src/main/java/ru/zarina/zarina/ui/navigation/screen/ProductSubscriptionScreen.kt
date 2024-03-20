package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.util.slideEnterTransition
import ru.zarina.zarina.ui.navigation.util.slidePopExitTransition
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenAction

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSubscription,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slideEnterTransition()

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ProductSubscriptionScreen(
            navigate = { action ->
                when (action) {
                    ProductSubscriptionScreenAction.ScreenClosed,
                    ProductSubscriptionScreenAction.SubscriptionCompleted -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSubscription.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
