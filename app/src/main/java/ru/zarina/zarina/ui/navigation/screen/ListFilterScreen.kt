package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.util.slideEnterTransition
import ru.zarina.zarina.ui.navigation.util.slidePopExitTransition
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreen
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenResult

fun NavGraphBuilder.listFilterScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.ListFilter,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Filters.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                UnscopedDestinations.Filters.routeSchema -> slidePopExitTransition()
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
