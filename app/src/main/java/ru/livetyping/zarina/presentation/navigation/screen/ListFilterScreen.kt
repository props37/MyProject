package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.presentation.model.filter.ListFilterParcelable
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreen
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreenResult
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.listFilterScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ListFilter,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.ProductFilters.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.ProductFilters.routeSchema -> slidePopExitTransition()
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
                        @Suppress("NAME_SHADOWING")
                        val result = UnscopedDestinations.ListFilter.Result(filterParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.ListFilter.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToListFilterScreen(filter: ListFilter<*>) {
    val args = UnscopedDestinations.ListFilter.Args(filter)
    this.navigate(
        route = UnscopedDestinations.ListFilter.routeSchema,
        args = UnscopedDestinations.ListFilter.createArgsBundle(args),
    )
}
