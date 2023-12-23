package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.graph.CatalogGraph
import ru.zarina.zarina.ui.screen.catalog.CatalogScreen

fun NavGraphBuilder.catalogGraph(navController: NavHostController) {
    navigationGraph(CatalogGraph) {
        composableDestination(CatalogGraph.Catalog) {
            CatalogScreen()
        }
    }
}
