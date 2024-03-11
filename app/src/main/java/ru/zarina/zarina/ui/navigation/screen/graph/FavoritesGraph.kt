package ru.zarina.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destination.graph.FavoritesGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesGraph(navController: NavHostController) {
    navigationGraph(FavoritesGraph) {
        composableDestination(FavoritesGraph.Favorites) {
            BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

            FavoritesScreen()
        }
    }
}
