package ru.livetyping.zarina.presentation.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.products.filters.FiltersScreen
import ru.livetyping.zarina.presentation.screen.products.filters.FiltersScreenAction
import ru.livetyping.zarina.presentation.screen.products.filters.FiltersScreenResult
import ru.livetyping.zarina.presentation.screen.products.filters.FiltersViewModel
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.filtersScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Filters,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> slideEnterTransition()
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
                UnscopedDestinations.Products.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        FiltersScreen(
            viewModel = hiltViewModel { factory: FiltersViewModel.Factory ->
                val listFilterResultFlow = it.savedStateHandle
                    .getStateFlow<UnscopedDestinations.ListFilter.Result?>(
                        key = UnscopedDestinations.ListFilter.RESULT_KEY,
                        initialValue = null,
                    )
                factory.create(listFilterResultFlow)
            },
            navigateForward = { action ->
                when (action) {
                    is FiltersScreenAction.ListFilterClicked -> {
                        navController.navigateToListFilterScreen(action.filter)
                    }
                }
            },
            navigateBackward = { result ->
                when (result) {
                    FiltersScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Filters.routeSchema,
                            inclusive = true,
                        )
                    }

                    is FiltersScreenResult.FiltersChanged -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Filters.routeSchema,
                            inclusive = true,
                        )
                        val filtersParcelable = FiltersParcelable.from(result.filters)
                        @Suppress("NAME_SHADOWING")
                        val result = UnscopedDestinations.Filters.Result(filtersParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.Filters.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToFiltersScreen(
    categoryId: Category.Id,
    filters: Filters? = null,
) {
    val args = UnscopedDestinations.Filters.Args(
        categoryId = categoryId,
        filters = filters,
    )
    this.navigate(
        route = UnscopedDestinations.Filters.routeSchema,
        args = UnscopedDestinations.Filters.createArgsBundle(args),
    )
}
