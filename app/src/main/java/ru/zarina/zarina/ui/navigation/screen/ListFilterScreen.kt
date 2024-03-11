package ru.zarina.zarina.ui.navigation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.navigation.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreen
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenResult

fun NavGraphBuilder.listFilterScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ListFilter,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Filters.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Filters.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        }
    ) {
        ListFilterScreen(
            navigateBackward = { result ->
                when (result) {
                    ListFilterScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ListFilter.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ListFilterScreenResult.FilterChanged -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ListFilter.routeSchema,
                            inclusive = true,
                        )
                        val filterParcelable = ListFilterParcelable.from(result.filter)
                        val result = UnscopedDestinations.ListFilter.Result(filterParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.ListFilter.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}
