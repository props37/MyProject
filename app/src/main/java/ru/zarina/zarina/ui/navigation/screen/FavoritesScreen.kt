package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.bottomnavbar.BottomNavBarItem
import ru.zarina.zarina.ui.bottomnavbar.navigateToBottomNavBarItem
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.CatalogGraph
import ru.zarina.zarina.ui.navigation.destination.graph.FavoritesGraph
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreen
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreenAction
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel
import ru.zarina.zarina.util.library.navigation.navigate

fun NavGraphBuilder.favoritesScreen(navController: NavHostController) {
    composableDestination(FavoritesGraph.Favorites) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        FavoritesScreen(
            viewModel = hiltViewModel { factory: FavoritesViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigate = { action ->
                when (action) {
                    FavoritesScreenAction.GoToCatalogClicked -> {
                        navController.navigateToBottomNavBarItem(BottomNavBarItem.Catalog)
                        navController.popBackStack(
                            route = CatalogGraph.Catalog.routeSchema,
                            inclusive = false,
                        )
                    }

                    is FavoritesScreenAction.AddProductToCartClicked -> {
                        val args = SizeSelectorGraph.SizeSelector.Args(action.product)
                        navController.navigate(
                            route = SizeSelectorGraph.routeSchema,
                            args = SizeSelectorGraph.createArgsBundle(args),
                        )
                    }
                }
            },
        )
    }
}
