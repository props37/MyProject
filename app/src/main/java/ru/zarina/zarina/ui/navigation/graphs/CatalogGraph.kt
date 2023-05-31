package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.categories.CategoriesScreen

fun NavGraphBuilder.catalogGraph() {
    navigationGraph(Catalog) {
        composableDestination(Catalog.Categories) {
            CategoriesScreen()
        }
    }
}
