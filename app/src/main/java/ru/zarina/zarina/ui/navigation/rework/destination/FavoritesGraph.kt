package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.graph.FavoritesGraph
import ru.zarina.zarina.util.compose.ScreenPlaceholder

fun NavGraphBuilder.favoritesGraph(navController: NavHostController) {
    navigationGraph(FavoritesGraph) {
        composableDestination(FavoritesGraph.Favorites) {
            ScreenPlaceholder(title = "Favorites")
        }
    }
}
