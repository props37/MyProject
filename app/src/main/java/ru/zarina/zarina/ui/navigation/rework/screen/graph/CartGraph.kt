package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CartGraph
import ru.zarina.zarina.ui.navigation.rework.screen.cartScreen

fun NavGraphBuilder.cartGraph(navController: NavHostController) {
    navigationGraph(CartGraph) {
        cartScreen(navController)
    }
}
