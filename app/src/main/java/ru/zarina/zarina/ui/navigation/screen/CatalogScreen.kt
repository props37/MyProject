package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.bottomnavbar.BottomNavBarItem
import ru.zarina.zarina.ui.bottomnavbar.navigateToBottomNavBarItem
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.CatalogGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.catalog.CatalogScreen
import ru.zarina.zarina.ui.screen.catalog.CatalogScreenAction

fun NavGraphBuilder.catalogScreen(
    navController: NavHostController,
) {
    composableDestination(CatalogGraph.Catalog) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        CatalogScreen(
            navigate = { action ->
                when (action) {
                    is CatalogScreenAction.CategoryClicked -> {
                        navController.navigateToProductsScreen(action.categoryId)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToCatalogScreen() {
    this.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
    this.popBackStack(
        route = CatalogGraph.Catalog.routeSchema,
        inclusive = false,
    )
}
