package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Favorites
import ru.zarina.zarina.ui.screens.favourites.FavoritesScreen

fun NavGraphBuilder.favoritesGraph(navController: NavController) {
    navigationGraph(Favorites) {
        composableDestination(Favorites.Root) {
            FavoritesScreen()
        }
    }
}
