package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Search
import ru.zarina.zarina.ui.screens.search.SearchScreen

fun NavGraphBuilder.searchGraph(navController: NavController) {
    navigationGraph(Search) {
        composableDestination(Search.Root) {
            SearchScreen()
        }
    }
}