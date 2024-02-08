package ru.zarina.zarina.ui.navigation.rework.destination.unscoped

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreen

fun NavGraphBuilder.productSubscriptionScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.ProductSubscription) {
        ProductSubscriptionScreen()
    }
}
