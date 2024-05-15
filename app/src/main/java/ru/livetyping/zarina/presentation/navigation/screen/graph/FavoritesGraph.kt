package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.FavoritesGraph
import ru.livetyping.zarina.presentation.navigation.screen.favoritesScreen

fun NavGraphBuilder.favoritesGraph(navController: NavHostController) {
    navigationGraph(FavoritesGraph) {
        favoritesScreen(navController)
    }
}
