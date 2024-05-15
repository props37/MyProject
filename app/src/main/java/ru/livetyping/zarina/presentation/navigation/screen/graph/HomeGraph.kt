package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.presentation.navigation.screen.homeScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigationGraph(HomeGraph) {
        homeScreen(navController)
    }
}
