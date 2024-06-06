package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersScreen
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersScreenAction
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersScreenResult
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersViewModel
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.productSearchFiltersScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ProductSearchFilters,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ProductSearch.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ListFilter.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ListFilter.routeSchema -> slidePopEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ProductSearch.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ProductSearchFiltersScreen(
            viewModel = hiltViewModel { factory: ProductSearchFiltersViewModel.Factory ->
                val listFilterResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.ListFilter.Result?>(
                        key = UnscopedDestinations.ListFilter.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(listFilterResultFlow)
            },
            navigateForward = { action ->
                when (action) {
                    is ProductSearchFiltersScreenAction.ListFilterClicked -> {
                        navController.navigateToListFilterScreen(action.filter)
                    }
                }
            },
            navigateBackward = { result ->
                when (result) {
                    ProductSearchFiltersScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSearchFilters.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ProductSearchFiltersScreenResult.FiltersChanged -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ProductSearchFilters.routeSchema,
                            inclusive = true,
                        )
                        val filtersParcelable = FiltersParcelable.from(result.filters)
                        @Suppress("NAME_SHADOWING")
                        val result = UnscopedDestinations.ProductSearchFilters.Result(filtersParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.ProductSearchFilters.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProductSearchFiltersScreen(
    searchQuery: String,
    filters: Filters? = null,
) {
    val args = UnscopedDestinations.ProductSearchFilters.Args(searchQuery, filters)
    this.navigate(
        route = UnscopedDestinations.ProductSearchFilters.routeSchema,
        args = UnscopedDestinations.ProductSearchFilters.createArgsBundle(args),
    )
}
