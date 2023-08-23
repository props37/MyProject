package ru.zarina.zarina.ui.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Search
import ru.zarina.zarina.ui.screens.common.filters.list.ListFilterScreen
import ru.zarina.zarina.ui.screens.search.SearchScreen
import ru.zarina.zarina.ui.screens.search.filters.FiltersScreen
import ru.zarina.zarina.ui.screens.search.selectsort.SelectSortScreen

fun NavGraphBuilder.searchGraph(navController: NavController) {
    navigationGraph(Search) {
        composableDestination(Search.Root) {
            SearchScreen(
                savedStateHandle = remember(it) { it.savedStateHandle },
                showProduct = { product ->
                    val arguments = Destinations.Product.Arguments(product.id)
                    navController.navigate(Destinations.Product.createRoute(arguments))
                },
                showSelectSort = {
                    navController.navigate(Search.SelectSort.routeSchema)
                },
                showFilters = {
                    navController.navigate(Search.Filters.routeSchema)
                }
            )
        }
        bottomSheetDestination(Search.SelectSort) {
            val searchSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Search.Root.routeSchema).savedStateHandle }
            SelectSortScreen(
                searchSavedStateHandle = searchSavedStateHandle,
                goBack = { navController.popBackStack(Search.SelectSort.routeSchema, true) }
            )
        }
        composableDestination(Search.Filters) {
            val savedStateHandle = remember(it) { it.savedStateHandle }
            val searchSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Search.Root.routeSchema).savedStateHandle }
            FiltersScreen(
                savedStateHandle = savedStateHandle,
                searchSavedStateHandle = searchSavedStateHandle,
                showFilter = { type ->
                    val arguments = Search.ListFilter.Arguments(filterType = type)
                    navController.navigate(Search.ListFilter.createRoute(arguments))
                },
                goBack = { navController.popBackStack(Search.Filters.routeSchema, true) }
            )
        }
        composableDestination(Search.ListFilter) {
            val filtersSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Search.Filters.routeSchema).savedStateHandle }
            ListFilterScreen(
                filtersSavedStateHandle = filtersSavedStateHandle,
                goBack = {
                    navController.popBackStack(Search.ListFilter.routeSchema, true)
                }
            )
        }
    }
}
