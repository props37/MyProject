package ru.zarina.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destination.graph.FavoritesGraph
import ru.zarina.zarina.ui.navigation.screen.favoritesScreen

fun NavGraphBuilder.favoritesGraph(navController: NavHostController) {
    navigationGraph(FavoritesGraph) {
        favoritesScreen(navController)
    }
}
