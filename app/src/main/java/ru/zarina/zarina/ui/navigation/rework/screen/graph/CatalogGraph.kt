package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CatalogGraph
import ru.zarina.zarina.ui.navigation.rework.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.catalog.CatalogScreen
import ru.zarina.zarina.ui.screen.catalog.CatalogScreenAction

fun NavGraphBuilder.catalogGraph(navController: NavHostController) {
    navigationGraph(CatalogGraph) {
        composableDestination(CatalogGraph.Catalog) {
            BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

            CatalogScreen(
                navigate = { action ->
                    when (action) {
                        is CatalogScreenAction.CategoryClicked -> {
                            val args = UnscopedDestinations.Products.Args(action.categoryId)
                            val route = UnscopedDestinations.Products.createRoute(args)
                            navController.navigate(route)
                        }
                    }
                },
            )
        }
    }
}
