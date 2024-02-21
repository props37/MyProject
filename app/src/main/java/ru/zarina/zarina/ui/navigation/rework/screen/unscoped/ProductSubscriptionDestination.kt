package ru.zarina.zarina.ui.navigation.rework.screen.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenAction

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.ProductSubscription) {
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
