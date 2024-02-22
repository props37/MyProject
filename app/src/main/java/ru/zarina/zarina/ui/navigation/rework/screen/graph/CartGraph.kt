package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.rework.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.cart.CartScreen

fun NavGraphBuilder.cartGraph(navController: NavHostController) {
    navigationGraph(CartGraph) {
        composableDestination(CartGraph.Cart) {
            BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

            CartScreen()
        }
    }
}
