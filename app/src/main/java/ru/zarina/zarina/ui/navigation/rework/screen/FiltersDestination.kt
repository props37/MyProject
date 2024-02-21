package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.filters.FiltersScreen
import ru.zarina.zarina.ui.screen.filters.FiltersScreenAction
import ru.zarina.zarina.ui.screen.filters.FiltersScreenResult
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel

fun NavGraphBuilder.filtersScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Filters,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ListFilter.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ListFilter.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Products.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

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
                        val route = UnscopedDestinations.ListFilter.createRoute(args)
                        navController.navigate(route)
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
