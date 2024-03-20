package ru.zarina.zarina.ui.navigation.screen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.util.slideEnterTransition
import ru.zarina.zarina.ui.navigation.util.slideExitTransition
import ru.zarina.zarina.ui.navigation.util.slidePopEnterTransition
import ru.zarina.zarina.ui.navigation.util.slidePopExitTransition
import ru.zarina.zarina.ui.screen.filters.FiltersScreen
import ru.zarina.zarina.ui.screen.filters.FiltersScreenAction
import ru.zarina.zarina.ui.screen.filters.FiltersScreenResult
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel
import ru.zarina.zarina.util.library.navigation.navigate

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
                factory.create(it.savedStateHandle)
            },
            navigateForward = { action ->
                when (action) {
                    is FiltersScreenAction.ListFilterClicked -> {
                        val args = UnscopedDestinations.ListFilter.Args(action.filter)
                        navController.navigate(
                            route = UnscopedDestinations.ListFilter.routeSchema,
                            args = UnscopedDestinations.ListFilter.createArgsBundle(args),
                        )
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
                        val result = UnscopedDestinations.Filters.Result(filtersParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.Filters.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}
