package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.screen.homeScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigationGraph(HomeGraph) {
        homeScreen(navController)
    }
}
