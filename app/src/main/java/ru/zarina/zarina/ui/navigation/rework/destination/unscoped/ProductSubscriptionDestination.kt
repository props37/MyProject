package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenResult

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.ProductSubscription) {
        ProductSubscriptionScreen(
            navigateBackward = { result ->
                when (result) {
                    ProductSubscriptionScreenResult.ScreenClosed -> {
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
