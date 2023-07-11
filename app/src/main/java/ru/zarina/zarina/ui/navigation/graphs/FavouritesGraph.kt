package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Favourites
import ru.zarina.zarina.ui.screens.favourites.FavouritesScreen

fun NavGraphBuilder.favouritesGraph(navController: NavController) {
    navigationGraph(Favourites) {
        composableDestination(Favourites.Root) {
            FavouritesScreen()
        }
    }
}
