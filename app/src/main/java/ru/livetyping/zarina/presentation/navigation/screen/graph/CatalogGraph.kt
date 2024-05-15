package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.screen.catalogScreen

fun NavGraphBuilder.catalogGraph(navController: NavHostController) {
    navigationGraph(CatalogGraph) {
        catalogScreen(navController)
    }
}
