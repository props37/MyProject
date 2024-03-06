package ru.zarina.zarina.ui.navigation.rework.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.rework.NavigationTransitionDurationMillis
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenAction

fun NavGraphBuilder.citySelectorBottomSheetScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.CitySelector,
        enterTransition = {
            when (initialState.destination.route) {
                UnscopedDestinations.Onboarding.routeSchema -> {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                CartGraph.Cart.routeSchema -> {
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
                UnscopedDestinations.Onboarding.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                CartGraph.Cart.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NavigationTransitionDurationMillis),
                    )
                }

                else -> null
            }
        },
    ) {
        CitySelectorScreen(
            navigate = { action ->
                when (action) {
                    CitySelectorScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CitySelectorScreenAction.CitySelected -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                        val cityParcelable = CityParcelable.from(action.city)
                        val result = UnscopedDestinations.CitySelector.Result(cityParcelable)
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.CitySelector.RESULT_KEY, result)
                    }
                }
            },
        )
    }
}
