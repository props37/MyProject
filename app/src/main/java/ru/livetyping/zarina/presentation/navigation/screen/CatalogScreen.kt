package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.CatalogGraph
import ru.livetyping.zarina.presentation.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreen
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreenAction

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
